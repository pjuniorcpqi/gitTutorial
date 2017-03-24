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

import com.cpqi.andes.model.Profile;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.ProfileService;
import com.google.common.collect.Lists;

@RestController
public class ProfileController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(ProfileController.class);

	@Autowired
	ProfileService profileService;

	@RequestMapping(value = "/profiles", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Profile>> listAll() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		List<Profile> profile = Lists.newArrayList(profileService.findAll());

		if (profile.isEmpty()) {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Profile>>(HttpStatus.NO_CONTENT);
		} else {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Profile>>(profile, HttpStatus.OK);
		}

	}
}
