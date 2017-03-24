package com.cpqi.andes.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.Holiday;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.HolidayService;
import com.cpqi.andes.util.DatesUtil;
import com.cpqi.andes.viewmodel.ResponseViewModel;
import com.google.common.collect.Lists;

/**
 *
 * @author tfacundo
 *
 */
@RestController
public class HolidayController extends AbstractController {
    
    private static final Logger	LOGGER = Logger.getLogger(HolidayController.class);
kajlsdfhjksadlfksadfhkajsdfmsadkf
    @Autowired
    private HolidayService	holidayService;

    @RequestMapping(value = "/holidays", method = RequestMethod.GET)
    public ResponseEntity<List<Holiday>> listAll() {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "listAll: List<Holidays>", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	final List<Holiday> holidayList = Lists.newArrayList(holidayService.findAll());

	if (holidayList.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "listAll: List<Holidays>", AuditAction.READ);
	    return new ResponseEntity<List<Holiday>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "listAll: List<Holidays>", AuditAction.READ);
	    return new ResponseEntity<List<Holiday>>(holidayList, HttpStatus.OK);
	}
    }

    @RequestMapping(value = "/holidays/month", method = RequestMethod.GET)
    public ResponseEntity<List<Holiday>> listAllHolidaysInAMonth(@RequestParam String month) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "listAllHolidaysInAMonth: List<Holidays>", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(),
	        month);

	final Calendar calendarMonth = DatesUtil.stringToCalendar("01-" + month, "dd-MMM-yy");

	final List<Holiday> holidayList = Lists.newArrayList(holidayService.findHolidayByMoth(calendarMonth));

	if (holidayList.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "listAllHolidaysInAMonth: List<Holidays>", AuditAction.READ);
	    return new ResponseEntity<List<Holiday>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "listAllHolidaysInAMonth: List<Holidays>", AuditAction.READ);
	    return new ResponseEntity<List<Holiday>>(holidayList, HttpStatus.OK);
	}
    }

    @RequestMapping(value = "/holidays/monthSite", method = RequestMethod.GET)
    public ResponseEntity<List<Holiday>> listAllHolidaysInMonthAndSite(@RequestParam String month, @RequestParam long idSite) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "listAllHolidaysInMonthAndSite: List<Holidays>", AuditAction.READ,
	        SecurityContextHolder.getContext().getAuthentication(), month, idSite);

	final Calendar calendarMonth = DatesUtil.stringToCalendar("01-" + month, "dd-MMM-yy");

	final List<Holiday> holidayList = Lists.newArrayList(holidayService.findHolidayByMonthAndSite(calendarMonth, idSite));

	if (holidayList.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "listAllHolidaysInMonthAndSite: List<Holidays>", AuditAction.READ);
	    return new ResponseEntity<List<Holiday>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "listAllHolidaysInMonthAndSite: List<Holidays>", AuditAction.READ);
	    return new ResponseEntity<List<Holiday>>(holidayList, HttpStatus.OK);
	}
    }

    /**
     *
     * @param holiday
     * @return
     */
    @RequestMapping(value = "/holidays/save", method = RequestMethod.POST)
    public ResponseEntity<Holiday> save(@RequestBody Holiday holiday) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "save: Holiday", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(), holiday);

	if (holiday != null) {
	    holidayService.save(holiday);
	    logInfoMethodExit(mark, LOGGER, "save: Holiday", AuditAction.CREATE);
	    return new ResponseEntity<Holiday>(holiday, HttpStatus.OK);
	} else {
	    logInfoMethodExit(mark, LOGGER, "save: Holiday", AuditAction.CREATE);
	    return new ResponseEntity<Holiday>(HttpStatus.NO_CONTENT);
	}
    }

    @RequestMapping(value = "/holidays/saveAll",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Holiday>> saveAllHolidays(@RequestBody List<Holiday> holidayList) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "saveAllHolidays: List<Holidays>", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(),
	        holidayList);

	if (holidayList != null && !holidayList.isEmpty()) {
	    
	    try {
		holidayService.save(holidayList);
	    }
	    catch (final Exception e) {
		logError(mark, LOGGER, e);
	    }

	    logInfoMethodExit(mark, LOGGER, "saveAllHolidays: List<Holidays>", AuditAction.CREATE);
	    return new ResponseEntity<List<Holiday>>(holidayList, HttpStatus.OK);
	} else {
	    logInfoMethodExit(mark, LOGGER, "saveAllHolidays: List<Holidays>", AuditAction.CREATE);
	    return new ResponseEntity<List<Holiday>>(HttpStatus.NO_CONTENT);
	}
    }

    @RequestMapping(value = "holidays/update", method = RequestMethod.POST)
    public ResponseEntity<ResponseViewModel> update(@RequestBody Holiday requestedHoliday) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "update", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), requestedHoliday);

	final ResponseViewModel ret = new ResponseViewModel();

	if (requestedHoliday != null) {
	    try {
		
		final Holiday holiday = holidayService.findById(requestedHoliday.getId());

		if (holiday != null) {
		    
		    logParamInfo(mark, LOGGER, "Holiday (old)", holiday);

		    holiday.setDescription(requestedHoliday.getDescription());
		    holidayService.save(holiday);

		} else {
		    throw new Exception("Holiday does not exist");
		}

		logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
		return ResponseEntity.status(HttpStatus.CREATED).body(ret);

	    }
	    catch (final Exception ex) {
		
		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);

		ret.setMsg(ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }
	} else {
	    ret.setMsg("Holiday does not exist");
	}
	logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
	return ResponseEntity.badRequest().body(ret);
    }

    /**
     * @param allocation
     * @return
     */
    @RequestMapping(value = "/holidays/deleteAll", method = RequestMethod.POST)
    public ResponseEntity<List<Holiday>> removeAll(@RequestBody List<Holiday> holidayList) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "removeAll:List<Holiday> ", AuditAction.DELETE, SecurityContextHolder.getContext().getAuthentication(), holidayList);

	holidayService.delete(holidayList);

	logInfoMethodExit(mark, LOGGER, "removeAll:List<Holiday> ", AuditAction.DELETE);
	return new ResponseEntity<List<Holiday>>(HttpStatus.NO_CONTENT);
    }
}
