/**
 * Copyright 2016 CPQi
 */
package com.cpqi.andes.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.AbsenceReason;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.AbsenceReasonService;
import com.google.common.collect.Lists;

/**
 * <p>
 * AbsenceReasonController
 * </p>
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@RestController
public class AbsenceReasonController extends AbstractController {
    private static final Logger	LOGGER = Logger.getLogger(AbsenceReasonController.class);

    @Autowired
    AbsenceReasonService	absenceReasonService;

    @RequestMapping(value = "/absenceReasons/", method = RequestMethod.GET)
    public ResponseEntity<List<AbsenceReason>> listAll() {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	List<AbsenceReason> absenceReason = Lists.newArrayList(absenceReasonService.findAll());

	if (absenceReason.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
	    return new ResponseEntity<List<AbsenceReason>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
	    return new ResponseEntity<List<AbsenceReason>>(absenceReason, HttpStatus.OK);
	}

    }
}
