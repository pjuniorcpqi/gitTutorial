package com.cpqi.andes.controller;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.configuration.DatabaseConfig;
import com.cpqi.andes.model.Allocation;
import com.cpqi.andes.model.Holiday;
import com.cpqi.andes.model.Principal;
import com.cpqi.andes.model.Settings;
import com.cpqi.andes.model.TimeLog;
import com.cpqi.andes.model.User;
import com.cpqi.andes.model.WorkLog;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.report.ReportFactory;
import com.cpqi.andes.service.AbsenceReasonService;
import com.cpqi.andes.service.AllocationService;
import com.cpqi.andes.service.HolidayService;
import com.cpqi.andes.service.SettingsService;
import com.cpqi.andes.service.TimeLogService;
import com.cpqi.andes.service.UserService;
import com.cpqi.andes.service.WorkLogService;
import com.cpqi.andes.util.DatesUtil;
import com.cpqi.andes.viewmodel.TimeSheetViewModel;

@RestController
public class TimeSheetController extends AbstractController {
    
    private static final Logger	 LOGGER	= Logger.getLogger(TimeSheetController.class);

    @Autowired
    private TimeLogService	 timeLogService;

    @Autowired
    private AllocationService	 allocationService;

    @Autowired
    private WorkLogService	 workLogService;

    @Autowired
    private UserService		 userService;

    @Autowired
    private HolidayService	 holidayService;

    @Autowired
    private SettingsService	 settingsService;

    @Autowired
    private AbsenceReasonService absenceReasonService;

    @Autowired
    private Environment		 env;

    @Autowired
    private ServletContext	 ser;

    @Autowired
    DatabaseConfig		 dbConf;

    @RequestMapping(value = "/userReport", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<byte[]> userReportTimeSheet(@RequestBody HashMap<String, Object> map) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "userReportTimeSheet", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), map);

	Map<String, Object> hm = null;

	String fileName = "userTimeSheet";
	Principal principal = null;

	ResponseEntity<byte[]> response = null;
	try {
	    if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
		principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    }

	    Long idUser = ((Integer) map.get("idUser")).longValue();
	    Object userName = map.get("userName");
	    Object yearMonth = map.get("yearMonth");
	    String requester = principal.getUsername();

	    hm = new HashMap<String, Object>();
	    hm.put("idUser", idUser);
	    hm.put("yearMonth", yearMonth);
	    hm.put("requesterName", requester);

	    ReportFactory factory = new ReportFactory(env);

	    byte[] contents = factory
	            .reportPDF(ser.getRealPath(File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "reports" + File.separator)
	                    + File.separator + fileName, hm, dbConf.dataSource());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));

	    headers.setContentDispositionFormData("attachment", userName + ".pdf");
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);

	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	}

	logInfoMethodExit(mark, LOGGER, "userReportTimeSheet", AuditAction.READ);
	return response;
    }

    @RequestMapping(value = "/timelogs/removeTimesheet", method = RequestMethod.POST)
    @PreAuthorize("isAdmin() or isOwner(#day)")
    public ResponseEntity<TimeSheetViewModel> removeTimeSheet(@RequestBody TimeSheetViewModel day) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "removeTimeSheet", AuditAction.DELETE, SecurityContextHolder.getContext().getAuthentication(), day);

	Settings settings = settingsService.findById(1);
	User user = userService.findByEmail(day.getUserName());
	User userRequesting = userService.findByEmail(day.getUserRequesting());

	if (!isNotAllowedToSubmit(day.getDate(), settings, user, userRequesting)) {
	    timeLogService.delete(day.getTimeLog());
	    for (WorkLog eachWorkLog : day.getWorkLogs()) {
		workLogService.delete(eachWorkLog);
	    }
	}

	logInfoMethodExit(mark, LOGGER, "removeTimeSheet", AuditAction.DELETE);
	return new ResponseEntity<TimeSheetViewModel>(day, HttpStatus.OK);
    }

    public static List<TimeSheetViewModel> getListTimeSheets() {
	return new ArrayList<TimeSheetViewModel>();
    }

    @RequestMapping(value = "/timelogs/getTimeSheet", method = RequestMethod.GET)
    @PreAuthorize("isAdmin() or isOwner(#idUser)")
    public ResponseEntity<List<TimeSheetViewModel>> getTimeSheet(@RequestParam long idUser, @RequestParam String date) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "getTimeSheet", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), idUser, date);

	Calendar dateCalendar = DatesUtil.stringToCalendar(date);
	Settings settings = settingsService.findById(1);

	User user = userService.findById(idUser);
	user.setPassword("");

	int daysInMonth = dateCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

	String firstDayOfMonth = "1" + "-" + (dateCalendar.get(Calendar.MONTH) + 1) + "-" + dateCalendar.get(Calendar.YEAR);
	String lastDayofMonth = daysInMonth + "-" + (dateCalendar.get(Calendar.MONTH) + 1) + "-" + dateCalendar.get(Calendar.YEAR);

	Calendar startDateCal = DatesUtil.stringToCalendar(firstDayOfMonth, DatesUtil.ddMMyyyyFormat);
	Calendar endDateCal = DatesUtil.stringToCalendar(lastDayofMonth, DatesUtil.ddMMyyyyFormat);

	List<TimeSheetViewModel> tList = new ArrayList<TimeSheetViewModel>(daysInMonth);

	List<WorkLog> workLogsFromMonth = workLogService.findByUserAndDateRange(idUser, startDateCal, endDateCal);

	List<TimeLog> timeLogsFromMonth = timeLogService.findAllByMonthAndUser(dateCalendar.get(Calendar.MONTH) + 1, idUser);

	for (TimeLog eachTimeLog : timeLogsFromMonth) {
	    eachTimeLog.getUser().setPassword("");
	}

	List<Allocation> allocationFromUser = allocationService.findByUserId(idUser);

	List<Holiday> holidaysFromMonth = holidayService.findHolidayByMonthAndSite(startDateCal, user.getSite().getId());

	for (int eachDay = 1; eachDay <= daysInMonth; eachDay++) {
	    Calendar dayToCheck = new GregorianCalendar(dateCalendar.get(Calendar.YEAR), dateCalendar.get(Calendar.MONTH), eachDay);

	    List<WorkLog> workLogsFromDay = new ArrayList<WorkLog>();
	    List<Allocation> allocationsFromDay = new ArrayList<Allocation>();
	    TimeLog timeLogFromDay = null;
	    Holiday holidayFromDay = null;

	    /*
	     * Check which allocation to insert each day from month(between
	     * allocation start and end period).
	     */
	    for (Allocation eachAllocation : allocationFromUser) {
		if ((dayToCheck.after(eachAllocation.getStartDate()) || dayToCheck.equals(eachAllocation.getStartDate()))
		        && (eachAllocation.getEndDate() == null || dayToCheck.before(eachAllocation.getEndDate())
		                || dayToCheck.equals(eachAllocation.getEndDate()))) {
		    allocationsFromDay.add(eachAllocation);
		}

	    }
	    /*
	     * Insert all worklogs already filled from each day from month.
	     */
	    for (WorkLog eachWorkLog : workLogsFromMonth) {
		if (eachWorkLog.getDate().equals(dayToCheck)) {
		    workLogsFromDay.add(eachWorkLog);
		}
	    }

	    /*
	     * If there's no worklog filled in day, insert new worklogs for each
	     * allocation.
	     */
	    if (workLogsFromDay.isEmpty()) {
		for (Allocation alocation : allocationsFromDay) {
		    WorkLog workLog = new WorkLog();
		    workLog.setAllocation(alocation);
		    workLog.setDate(dayToCheck);
		    workLogsFromDay.add(workLog);
		}
	    }

	    /*
	     * If the worklogs from day doesn't match the allocations from day,
	     * insert new worklogs to match the allocation number.
	     */
	    if (workLogsFromDay.size() < allocationsFromDay.size() && workLogsFromDay.size() > 0) {
		for (Allocation eachAllocation : allocationsFromDay) {
		    WorkLog workLog = null;

		    for (WorkLog eachWorklog : workLogsFromMonth) {
			if (eachWorklog.getAllocation().getId() == eachAllocation.getId() && eachWorklog.getDate().equals(dayToCheck)) {
			    workLog = eachWorklog;
			    break;
			}
		    }
		    if (workLog == null) {
			workLog = new WorkLog();
			workLog.setAllocation(eachAllocation);
			workLog.setDate(dayToCheck);
			workLog.setTimeInserted(null);

			workLogsFromDay.add(workLog);
		    }
		}
	    }
	    /*
	     * Insert all timeLogs from month already filled from each day from
	     * month
	     */
	    for (TimeLog eachTimeLog : timeLogsFromMonth) {
		if (eachTimeLog.getDate().equals(dayToCheck)) {
		    timeLogFromDay = eachTimeLog;
		}
	    }

	    /*
	     * Check if current day is holiday.
	     */
	    for (Holiday eachHoliday : holidaysFromMonth) {
		if (dayToCheck.equals(eachHoliday.getHolidayDate())) {
		    holidayFromDay = eachHoliday;
		}
	    }

	    TimeSheetViewModel timesViewModel = new TimeSheetViewModel();
	    timesViewModel.setDate(dayToCheck);
	    timesViewModel.setTimeLog(timeLogFromDay);
	    timesViewModel.setWorkLogs(workLogsFromDay);
	    timesViewModel.setAllocations(allocationsFromDay);
	    timesViewModel.setHoliday(holidayFromDay);
	    timesViewModel.setBlocked(isNotAllowedToSubmit(dayToCheck, settings, user, user));
	    tList.add(timesViewModel);
	}

	logInfoMethodExit(mark, LOGGER, "getTimeSheet", AuditAction.READ);
	return new ResponseEntity<List<TimeSheetViewModel>>(tList, HttpStatus.OK);
    }

    private boolean isNotAllowedToSubmit(Calendar dayToCheck, Settings settings, User user, User userRequesting) {
	boolean isNotAllowed = true;
	String accessLevel = userRequesting.getAccessLevel().getDescription();
	if (accessLevel.equalsIgnoreCase("manager") || accessLevel.equalsIgnoreCase("admin")) {
	    isNotAllowed = false;
	} else {
	    Calendar systemDate = Calendar.getInstance();
	    int daysPeriod = settings.getBlockTimeSheet();

	    Calendar afterFilter = (Calendar) systemDate.clone();
	    afterFilter.add(Calendar.DAY_OF_MONTH, daysPeriod);
	    Calendar beforeFilter = (Calendar) systemDate.clone();
	    beforeFilter.add(Calendar.DAY_OF_MONTH, -daysPeriod);

	    boolean afterFilterBefore = dayToCheck.after(beforeFilter);
	    boolean beforeFilterAfter = dayToCheck.before(afterFilter);

	    boolean sameDayBeforeFilter = sameDayFilter(dayToCheck, beforeFilter);
	    boolean sameDayAfterFilter = sameDayFilter(dayToCheck, beforeFilter);

	    isNotAllowed = !((sameDayBeforeFilter || afterFilterBefore) && (sameDayAfterFilter || beforeFilterAfter));

	}
	return isNotAllowed;
    }

    @RequestMapping(value = "/timelogsAdmin/markAsAbsent", method = RequestMethod.POST)
    @PreAuthorize("isAdmin()")
    public ResponseEntity<TimeSheetViewModel> markAsAbsent(@RequestBody TimeSheetViewModel day) {
	long mark = new Date().getTime();
	try {
	    logInfoMethodEntry(mark, LOGGER, "markAsAbsent", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), day);

	    if (day.getTimeLog() != null) {
		removeTimeSheet(day);
	    }

	    TimeLog timelog = new TimeLog();
	    timelog.setUser(userService.findByEmail(day.getUserName()));
	    timelog.setDate(day.getDate());
	    timelog.setAbsenceReason(absenceReasonService.findById(day.getAbsenceReasonId()));
	    timeLogService.save(timelog);
	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	    return new ResponseEntity<TimeSheetViewModel>(day, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	logInfoMethodExit(mark, LOGGER, "markAsAbsent", AuditAction.SAVE_UPDATE);

	return new ResponseEntity<TimeSheetViewModel>(day, HttpStatus.OK);
    }

    @RequestMapping(value = "/timelogsAdmin/unmarkAsAbsent", method = RequestMethod.POST)
    @PreAuthorize("isAdmin()")
    public ResponseEntity<TimeSheetViewModel> unmarkAsAbsent(@RequestBody TimeSheetViewModel day) {
	long mark = new Date().getTime();
	try {
	    logInfoMethodEntry(mark, LOGGER, "unmarkAsAbsent", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), day);

	    removeTimeSheet(day);
	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	    return new ResponseEntity<TimeSheetViewModel>(day, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	logInfoMethodExit(mark, LOGGER, "unmarkAsAbsent", AuditAction.SAVE_UPDATE);

	return new ResponseEntity<TimeSheetViewModel>(day, HttpStatus.OK);
    }

    private boolean sameDayFilter(Calendar dayToCheck, Calendar filterDate) {
	boolean isSameDay = false;

	if (dayToCheck.get(Calendar.YEAR) == filterDate.get(Calendar.YEAR)) {
	    isSameDay = dayToCheck.get(Calendar.DAY_OF_YEAR) == filterDate.get(Calendar.DAY_OF_YEAR);
	}

	return isSameDay;
    }

    @RequestMapping(value = "timelogs/saveTimeSheet", method = RequestMethod.POST)
    @PreAuthorize("isAdmin() or isOwner(#day)")
    public ResponseEntity<TimeSheetViewModel> saveTimeSheet(@RequestBody TimeSheetViewModel day) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "saveTimeSheet", AuditAction.SAVE_UPDATE, SecurityContextHolder.getContext().getAuthentication(), day);

	HttpStatus status;
	User user = userService.findByEmail(day.getUserName());
	User userRequesting = userService.findByEmail(day.getUserRequesting());
	Settings settings = settingsService.findById(1);

	if (!isNotAllowedToSubmit(day.getDate(), settings, user, userRequesting)) {
	    if (day.getAbsenceReasonId() == 0) {
		TimeLog timeLog = day.getTimeLog();
		Long idUser = user.getId();
		Calendar dateTimeLog = day.getDate();

		String[] timelogs = getTimeLogsfromUser(day, user, timeLog);

		boolean validInput = true;
		boolean hasEmptyField = false;
		int lastTimeLogMinutes = -1;
		int sumOfTimelogsInMinutes = 0;

		for (int i = 0; i < 6; i++) {
		    if (isNullOrEmpty(timelogs[i])) {
			hasEmptyField = true;
		    } else {
			/*
			 * 1) Nao podem existir timelogs preenchidos a direita de um
			 * timelog vazio. 2) O tempo de cada timelog deve ser
			 * posterior ao do timelog a sua esquerda (ultimo). 3) A
			 * quantidade de minutos de um timelog nao pode ser maior ou
			 * igual a 60.
			 */
			int thisTimelogInMinutes = timeStringToMinutes(timelogs[i]);
			if (hasEmptyField || lastTimeLogMinutes > thisTimelogInMinutes || timelogHasInvalidMinutes(timelogs[i])) {
			    validInput = false;
			    break;
			} else {
			    /*
			     * Calcula a quantidade de tempo trabalhado a partir da
			     * soma dos timelogs de 2 em 2.
			     */
			    if (i % 2 != 0) {
				sumOfTimelogsInMinutes += thisTimelogInMinutes - lastTimeLogMinutes;
			    }
			    lastTimeLogMinutes = thisTimelogInMinutes;
			}
		    }
		}

		/*
		 * 4) A soma de tempo dos worklogs nao pode ser maior que o tempo
		 * total dos timelogs.
		 */
		BigDecimal sumOfWorkLogsTime = BigDecimal.ZERO;
		for (WorkLog wl : day.getWorkLogs()) {
		    if (wl.getTimeInserted() != null) {
			sumOfWorkLogsTime = sumOfWorkLogsTime.add(wl.getTimeInserted());
		    }
		}
		sumOfWorkLogsTime = sumOfWorkLogsTime.round(new MathContext(6, RoundingMode.HALF_UP));

		validInput = timeDifferenceCompatible(sumOfWorkLogsTime, sumOfTimelogsInMinutes, 1);

		if (validInput) {
		    TimeLog existingTimeLog = timeLogService.findByDateAndUser(dateTimeLog, idUser);
		    if (existingTimeLog != null) {
			timeLogService.delete(existingTimeLog);
		    }

		    for (WorkLog eachWorkLog : day.getWorkLogs()) {
			Calendar dateWorkLog = eachWorkLog.getDate();
			Long idAllocation = eachWorkLog.getAllocation().getId();
			WorkLog workLogDate = workLogService.findByDateAndAllocation(dateWorkLog, idAllocation);
			if (eachWorkLog != null && workLogDate == null) {
			    workLogService.save(eachWorkLog);
			} else if (!workLogDate.isRevenueLock()) {
			    workLogDate.setTimeInserted(eachWorkLog.getTimeInserted());
			    workLogDate.setComments(eachWorkLog.getComments());
			    workLogService.save(workLogDate);
			}

		    }
		    timeLogService.save(timeLog);
		    status = HttpStatus.OK;
		} else {
		    status = HttpStatus.UNPROCESSABLE_ENTITY;
		}
	    } else {
		status = HttpStatus.PRECONDITION_FAILED;
	    }
	} else {
	    status = HttpStatus.UNPROCESSABLE_ENTITY;
	}

	logInfoMethodExit(mark, LOGGER, "saveTimeSheet", AuditAction.SAVE_UPDATE);
	return new ResponseEntity<TimeSheetViewModel>(day, status);

    }

    private String[] getTimeLogsfromUser(TimeSheetViewModel day, User user, TimeLog timeLog) {
	timeLog.setUser(user);
	timeLog.setDate(day.getDate());

	String[] timelogs = { timeLog.getInTime1(), timeLog.getOutTime1(), timeLog.getInTime2(), timeLog.getOutTime2(), timeLog.getInTime3(),
	        timeLog.getOutTime3() };
	return timelogs;
    }

    @RequestMapping(value = "/timelogsAdmin/saveByAdmin", method = RequestMethod.POST)
    public ResponseEntity<TimeSheetViewModel> savedByAdmin(@RequestBody TimeSheetViewModel day) {
	day.getTimeLog().setInsertedByAdmin(true);
	return saveTimeSheet(day);

    }

    @RequestMapping(value = "/timelogsAdmin/removeByAdmin", method = RequestMethod.POST)
    public ResponseEntity<TimeSheetViewModel> removedByAdmin(@RequestBody TimeSheetViewModel day) {
	day.getTimeLog().setInsertedByAdmin(true);
	return removeTimeSheet(day);
    }

    @RequestMapping(value = "/timelogsAdmin/lockedByAdmin")
    @PreAuthorize("isAdmin() or isOwner(#idUser)")
    public ResponseEntity<List<TimeSheetViewModel>> lockedByAdmin(@RequestBody List<TimeSheetViewModel> timesheets) {
	long mark = new Date().getTime();
	try {
	    logInfoMethodEntry(mark, LOGGER, "lockedByAdmin", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), timesheets);

	    for (TimeSheetViewModel eachTimeSheet : timesheets) {
		List<WorkLog> workLogsFromDay = eachTimeSheet.getWorkLogs();
		for (WorkLog eachWorkLogFromDay : workLogsFromDay) {
		    eachWorkLogFromDay.setRevenueLock(true);
		}
		workLogService.save(workLogsFromDay);
	    }
	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	}

	logInfoMethodExit(mark, LOGGER, "lockedByAdmin", AuditAction.SAVE_UPDATE);

	return new ResponseEntity<List<TimeSheetViewModel>>(timesheets, HttpStatus.OK);
    }

    @RequestMapping(value = "/timelogsAdmin/unlockedByAdmin")
    public ResponseEntity<List<TimeSheetViewModel>> unlockedByAdmin(@RequestBody List<TimeSheetViewModel> timesheets) {
	long mark = new Date().getTime();
	try {
	    logInfoMethodEntry(mark, LOGGER, "unlockedByAdmin", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), timesheets);

	    for (TimeSheetViewModel eachTimeSheet : timesheets) {
		List<WorkLog> workLogsFromDay = eachTimeSheet.getWorkLogs();
		for (WorkLog eachWorkLogFromDay : workLogsFromDay) {
		    eachWorkLogFromDay.setRevenueLock(false);
		}
		workLogService.save(workLogsFromDay);
	    }
	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	}

	logInfoMethodExit(mark, LOGGER, "lockedByAdmin", AuditAction.SAVE_UPDATE);

	return new ResponseEntity<List<TimeSheetViewModel>>(timesheets, HttpStatus.OK);
    }

    private boolean timeDifferenceCompatible(BigDecimal sumOfWorkLogsTime, int sumOfTimelogsInMinutes, int delta) {
	
	int workLogInHours = sumOfWorkLogsTime.intValue();
	int workLogInMinutes = sumOfWorkLogsTime.remainder(BigDecimal.ONE).multiply(new BigDecimal(60)).intValue();

	int workLogTotalMinutes = workLogInHours * 60 + workLogInMinutes;

	if (Math.abs(workLogTotalMinutes - sumOfTimelogsInMinutes) <= delta) {
	    return true;
	} else {
	    return false;
	}

    }

    private boolean isNullOrEmpty(String str) {
	return str == null || str.isEmpty();
    }

    private int timeStringToMinutes(String time) {
	int hours = 0;
	int minutes = 0;

	if (!isNullOrEmpty(time)) {
	    String[] aux = time.split(":");
	    hours = Integer.parseInt(aux[0]);
	    minutes = Integer.parseInt(aux[1]);
	}

	return hours * 60 + minutes;
    }

    private boolean timelogHasInvalidMinutes(String time) {
	return Integer.parseInt(time.split(":")[1]) >= 60 ? true : false;
    }
}