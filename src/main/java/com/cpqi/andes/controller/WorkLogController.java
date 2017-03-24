package com.cpqi.andes.controller;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.WorkLog;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.WorkLogService;

@RestController
public class WorkLogController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(WorkLogController.class);

	@Autowired
	private WorkLogService service;

	@RequestMapping(value = "/worklogs/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<WorkLog> save(@RequestBody WorkLog w) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "save", AuditAction.SAVE_UPDATE,
				SecurityContextHolder.getContext().getAuthentication(), w);

		Calendar date = w.getDate();
		Long idAllocation = w.getAllocation().getId();

		WorkLog workLogDate = service.findByDateAndAllocation(date, idAllocation);

		logParamInfo(mark, LOGGER, "WorkLog (old)", workLogDate);

		if (w != null && workLogDate == null) {
			service.save(w);
		} else {
			w.setId(workLogDate.getId());
			service.save(w);

		}

		logInfoMethodExit(mark, LOGGER, "save", AuditAction.SAVE_UPDATE);
		return new ResponseEntity<WorkLog>(w, HttpStatus.CREATED);
	}
}
