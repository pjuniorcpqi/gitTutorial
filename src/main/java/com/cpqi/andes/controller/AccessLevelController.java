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

import com.cpqi.andes.model.AccessLevel;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.AccessLevelService;
import com.google.common.collect.Lists;

/**
 * <p>
 * SiteController
 * </p>
 * .
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @version 0.1
 * @since 0.1
 */

@RestController
public class AccessLevelController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(AccessLevelController.class);

	@Autowired
	private AccessLevelService service;

	/**
	 * List all access levels.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/accessLevels/", method = RequestMethod.GET)
	public ResponseEntity<List<AccessLevel>> listAll() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll: List<AccessLevel>", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		List<AccessLevel> accessLevels = Lists.newArrayList(service.findAll());

		if (accessLevels.isEmpty()) {
			logInfoMethodExit(mark, LOGGER, "listAll: List<AccessLevel>", AuditAction.READ);
			return new ResponseEntity<List<AccessLevel>>(HttpStatus.NO_CONTENT);
		} else {
			logInfoMethodExit(mark, LOGGER, "listAll: List<AccessLevel>", AuditAction.READ);
			return new ResponseEntity<List<AccessLevel>>(accessLevels, HttpStatus.OK);
		}
	}

}
