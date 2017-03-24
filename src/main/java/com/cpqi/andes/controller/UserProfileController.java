package com.cpqi.andes.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.UserProfile;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.UserProfileService;

@RestController
public class UserProfileController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(UserProfileController.class);

	@Autowired
	UserProfileService userProfileService;

	@RequestMapping(value = "/userProfile")
	public ResponseEntity<UserProfile> getByID(@RequestParam long id) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "getByID", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication(), id);

		UserProfile userProfile = userProfileService.findById(id);

		logInfoMethodExit(mark, LOGGER, "getByID", AuditAction.READ);
		return new ResponseEntity<UserProfile>(userProfile, HttpStatus.OK);
	}
}
