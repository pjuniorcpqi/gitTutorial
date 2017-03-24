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

import com.cpqi.andes.model.ProjectType;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.ProjectTypeService;
import com.google.common.collect.Lists;

@RestController
public class ProjectTypeController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(ProjectTypeController.class);

	@Autowired
	ProjectTypeService projectTypeService;

	@RequestMapping(value = "/projectTypes/", method = RequestMethod.GET)
	public ResponseEntity<List<ProjectType>> listAll() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		List<ProjectType> projectTypes = Lists.newArrayList(projectTypeService.findAll());

		if (projectTypes.isEmpty()) {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<ProjectType>>(HttpStatus.NO_CONTENT);
		} else {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<ProjectType>>(projectTypes, HttpStatus.OK);
		}

	}

}
