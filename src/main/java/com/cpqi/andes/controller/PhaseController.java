package com.cpqi.andes.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.Phase;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.PhaseService;
import com.google.common.collect.Lists;

@RestController
public class PhaseController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(PhaseController.class);

	@Autowired
	PhaseService phaseService;

	@RequestMapping(value = "/phase/save")
	public ResponseEntity<Phase> saveNew(@RequestBody Phase phase) {

		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "saveNew", AuditAction.CREATE,
				SecurityContextHolder.getContext().getAuthentication(), phase);

		phaseService.save(phase);

		logInfoMethodExit(mark, LOGGER, "saveNew", AuditAction.CREATE);
		return new ResponseEntity<Phase>(phase, HttpStatus.NO_CONTENT);

	}

	@RequestMapping(value = "/phases")
	public ResponseEntity<List<Phase>> listAll() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		List<Phase> phases = Lists.newArrayList(phaseService.findAll());

		if (phases.isEmpty()) {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Phase>>(HttpStatus.NO_CONTENT);
		} else {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Phase>>(phases, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/phase/delete")
	public ResponseEntity<Phase> delete(@RequestBody Phase phase) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "delete", AuditAction.DELETE,
				SecurityContextHolder.getContext().getAuthentication());

		phaseService.delete(phase);

		logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);
		return new ResponseEntity<Phase>(phase, HttpStatus.NO_CONTENT);
	}
}
