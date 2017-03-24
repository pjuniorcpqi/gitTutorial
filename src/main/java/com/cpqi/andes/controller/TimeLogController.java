package com.cpqi.andes.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.TimeLog;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.TimeLogService;

/**
 * <p>
 * TimeLogController
 * </p>
 *
 * @author Pedro Marcos <pjunior@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@RestController
public class TimeLogController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(TimeLogController.class);

	@Autowired
	private TimeLogService timeLogService;

	/**
	 * @param timeLog
	 * @return
	 */
	@RequestMapping(value = "/timelogs/save")
	public ResponseEntity<TimeLog> save(@RequestBody TimeLog timeLog) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "save", AuditAction.SAVE_UPDATE,
				SecurityContextHolder.getContext().getAuthentication(), timeLog);

		Calendar date = timeLog.getDate();
		Long idUser = timeLog.getUser().getId();
		TimeLog existingTimeLog = timeLogService.findByDateAndUser(date, idUser);

		if (existingTimeLog != null) {
			logParamInfo(mark, LOGGER, "TimeLog (old)", existingTimeLog);
			timeLog.setId(existingTimeLog.getId());
		}

		timeLogService.save(timeLog);

		logInfoMethodExit(mark, LOGGER, "save", AuditAction.SAVE_UPDATE);
		return new ResponseEntity<TimeLog>(timeLog, HttpStatus.NO_CONTENT);
	}

	/**
	 * Find timeLog by user and date.
	 *
	 * @param name
	 *            the site's name
	 * @return the response entity
	 */
	@RequestMapping(value = "/timelogs", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<TimeLog>> findByMonthAndUser(@RequestParam(name = "month") String month,
			@RequestParam(name = "idUser") Long idUser) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "findByMonthAndUser", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication(), month, idUser);

		int monthParam = Integer.parseInt(month);
		monthParam++;// To compensate the fact that months are limited
		// to 0-11 in Java.

		List<TimeLog> timeLogList = new ArrayList<TimeLog>();
		timeLogList = timeLogService.findAllByMonthAndUser(monthParam, idUser);

		if (timeLogList.isEmpty() == false) {

			logInfoMethodExit(mark, LOGGER, "findByMonthAndUser", AuditAction.READ);
			return new ResponseEntity<List<TimeLog>>(timeLogList, HttpStatus.OK);

		} else {

			logInfoMethodExit(mark, LOGGER, "findByMonthAndUser", AuditAction.READ);
			return new ResponseEntity<List<TimeLog>>(HttpStatus.NO_CONTENT);
		}

	}

}
