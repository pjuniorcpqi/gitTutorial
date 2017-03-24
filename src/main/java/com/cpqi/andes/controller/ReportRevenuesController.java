package com.cpqi.andes.controller;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.configuration.DatabaseConfig;
import com.cpqi.andes.model.Allocation;
import com.cpqi.andes.model.Principal;
import com.cpqi.andes.model.Project;
import com.cpqi.andes.model.ReportFilterUserByDate;
import com.cpqi.andes.model.WorkLog;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.report.ReportFactory;
import com.cpqi.andes.service.ReportRevenueService;
import com.cpqi.andes.service.WorkLogService;
import com.cpqi.andes.viewmodel.ReportRevenueViewModel;
import com.cpqi.andes.viewmodel.UserLogViewModel;

/**
 * The Class ReportRevenuesController.
 */
@RestController
public class ReportRevenuesController extends AbstractController {
    
    private static final Logger	LOGGER = Logger.getLogger(ReportRevenuesController.class);
    
    /** The report service. */
    @Autowired
    ReportRevenueService	reportService;

    @Autowired
    WorkLogService		worklogService;

    /** The env. */
    @Autowired
    private Environment		env;

    /** The ser. */
    @Autowired
    private ServletContext	ser;

    /** The db conf. */
    @Autowired
    DatabaseConfig		dbConf;

    /**
     * User report time sheet.
     *
     * @param map
     *            the map
     * @return the response entity
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/revenueReport", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<byte[]> revenueReportGenerator(@RequestBody HashMap<String, Object> map) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "revenueReportGenerator", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), map);
	
	Map<String, Object> hm = null;

	String fileName = "revenueReport";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Date parsedDate = null;
	Principal principal = null;

	ResponseEntity<byte[]> response = null;
	try {
	    if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
		principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    }

	    ArrayList<Long> userId = (ArrayList<Long>) map.get("userId");
	    ArrayList<Long> clientId = (ArrayList<Long>) map.get("clientId");
	    ArrayList<Long> projectId = (ArrayList<Long>) map.get("projectId");
	    ArrayList<Long> phaseId = (ArrayList<Long>) map.get("phaseId");
	    parsedDate = sdf.parse((String) map.get("startDate"));
	    Timestamp startDate = new Timestamp(parsedDate.getTime());
	    parsedDate = sdf.parse((String) map.get("endDate"));
	    Timestamp endDate = new Timestamp(parsedDate.getTime());
	    
	    String requester = principal.getUsername();

	    hm = new HashMap<String, Object>();
	    hm.put("userId", userId);
	    hm.put("clientId", clientId);
	    hm.put("projectId", projectId);
	    hm.put("phaseId", phaseId);
	    hm.put("startDate", startDate);
	    hm.put("endDate", endDate);
	    hm.put("requesterName", requester);

	    ReportFactory factory = new ReportFactory(env);

	    byte[] contents = factory
	            .reportPDF(ser.getRealPath(File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "reports" + File.separator)
	                    + File.separator + fileName, hm, dbConf.dataSource());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);

	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	    logInfoMethodExit(mark, LOGGER, "revenueReportGenerator", AuditAction.READ);
	    
	    e.printStackTrace();
	}

	logInfoMethodExit(mark, LOGGER, "revenueReportGenerator", AuditAction.READ);
	return response;
    }

    /**
     * Search.
     *
     * @param reportFilter
     *            the report filter
     * @return the response entity
     */
    @RequestMapping(value = "report/revenues", method = RequestMethod.POST)
    public ResponseEntity<ArrayList<ReportRevenueViewModel>> search(@RequestBody ReportFilterUserByDate reportFilter) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "search", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), reportFilter);

	final Map<Long, ReportRevenueViewModel> viewModelMap = new HashMap<Long, ReportRevenueViewModel>(0);

	if (reportFilter == null) {
	    logInfoMethodExit(mark, LOGGER, "search", AuditAction.READ);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<ReportRevenueViewModel>(viewModelMap.values()));
	}

	if (reportFilter.getEndDate() == null) {
	    reportFilter.setEndDate(Calendar.getInstance());
	}
	if (reportFilter.getClientId() == null) {
	    reportFilter.setClientId(Arrays.asList(0L));
	}
	if (reportFilter.getPhaseId() == null) {
	    reportFilter.setPhaseId(Arrays.asList(0L));
	}
	if (reportFilter.getProjectId() == null) {
	    reportFilter.setProjectId(Arrays.asList(0L));
	}
	if (reportFilter.getUserId() == null) {
	    reportFilter.setUserId(Arrays.asList(0L));
	}

	try {
	    
	    final ArrayList<WorkLog> selectWorkLog = reportService.getRevenue(reportFilter.getUserId(), reportFilter.getStartDate(), reportFilter.getEndDate(),
	            reportFilter.getProjectId(), reportFilter.getClientId(), reportFilter.getPhaseId());

	    if (selectWorkLog.size() > 0) {
		
		Project proj = null;
		UserLogViewModel tempLog = null;
		ReportRevenueViewModel tempReport = null;

		for (final WorkLog workLog : selectWorkLog) {
		    proj = workLog.getAllocation().getPhase().getProject();

		    tempReport = viewModelMap.get(proj.getId());

		    if (tempReport == null) {
			tempReport = new ReportRevenueViewModel();
			tempReport.setIdProject(proj.getId());
			tempReport.setProjectName(proj.getTitle());
			tempReport.setClientName(proj.getClient().getName());
			tempReport.setProjectCurrency(proj.getCurrency().getCode());

			viewModelMap.put(proj.getId(), tempReport);
		    }

		    tempReport.setTotalHours(tempReport.getTotalHours().add(calculeTimesInserted(workLog)));

		    tempLog = thisArrayContainsThisAllocation(tempReport.getAllocations(), workLog.getAllocation());

		    if (tempLog == null) {
			tempLog = new UserLogViewModel();
			tempLog.setIdAllocation(workLog.getAllocation().getId());
			tempLog.setProfileName(workLog.getAllocation().getProfile().getDescription());
			tempLog.setUserName(workLog.getAllocation().getUser().getName());
			tempLog.setPrice(workLog.getAllocation().getPrice());
			tempLog.setPhaseDescription(workLog.getAllocation().getPhase().getDescription());
			tempReport.getAllocations().add(tempLog);
		    }

		    tempLog.setTotalHours(tempLog.getTotalHours().add(validBigDecimal(workLog.getTimeInserted())));
		    tempLog.setTotalHoursOffset(tempLog.getTotalHoursOffset().add(validBigDecimal(workLog.getTimeOffsetAdmin())));

		}

		final Set<Long> keySet = viewModelMap.keySet();
		for (final Long key : keySet) {
		    tempReport = viewModelMap.get(key);
		    if (tempReport != null) {
			for (final UserLogViewModel alloc : tempReport.getAllocations()) {
			    tempReport.setTotalAmount(tempReport.getTotalAmount().add(calculeAmount(alloc)));
			}
		    }
		}
	    }

	}
	catch (final Exception ex) {
	    logError(mark, LOGGER, ex);
	    logInfoMethodExit(mark, LOGGER, "search", AuditAction.READ);
	    
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<ReportRevenueViewModel>(viewModelMap.values()));
	}

	logInfoMethodExit(mark, LOGGER, "search", AuditAction.READ);
	return ResponseEntity.ok().body(new ArrayList<ReportRevenueViewModel>(viewModelMap.values()));
    }

    /**
     * Método auxiliar.
     *
     * @param allocList
     *            the alloc list
     * @param alloc
     *            the alloc
     * @return the user log view model
     */
    private UserLogViewModel thisArrayContainsThisAllocation(List<UserLogViewModel> allocList, Allocation alloc) {
	
	if (allocList == null || allocList.isEmpty()) {
	    return null;
	}

	if (alloc == null) {
	    return null;
	}

	for (final UserLogViewModel view : allocList) {
	    if (alloc.getId() == view.getIdAllocation()) {
		return view;
	    }
	}

	return null;
    }

    /**
     * Calcule amount.
     *
     * @param tempLog
     *            the temp log
     * @return the big decimal
     */
    private BigDecimal calculeAmount(UserLogViewModel tempLog) {
	if (tempLog.getTotalHours().add(tempLog.getTotalHoursOffset()).compareTo(BigDecimal.ZERO) < 1) {
	    return BigDecimal.ZERO;
	}

	final BigDecimal sum = tempLog.getTotalHours().add(tempLog.getTotalHoursOffset());
	
	return sum.multiply(validBigDecimal(tempLog.getPrice()));
    }

    /**
     * Calcule times inserted.
     *
     * @param workLog
     *            the work log
     * @return the big decimal
     */
    private BigDecimal calculeTimesInserted(WorkLog workLog) {
	
	final BigDecimal timeInserted = validBigDecimal(workLog.getTimeInserted());
	final BigDecimal timeOffset = validBigDecimal(workLog.getTimeOffsetAdmin());

	return timeInserted.add(timeOffset);
    }

    /**
     * Valid big decimal.
     *
     * @param value
     *            the value
     * @return the big decimal
     */
    private BigDecimal validBigDecimal(BigDecimal value) {
	return value != null ? value : BigDecimal.ZERO;
    }
}
