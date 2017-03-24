/**
 * Copyright 2016 CPQi
 */
package com.cpqi.andes.controller;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.User;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.UserService;

/**
 * <p>
 * ActivationController
 * </p>
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@RestController
public class ActivationController extends AbstractController {
    
    private static final Logger	LOGGER = Logger.getLogger(ActivationController.class);

    /** The service. */
    @Autowired
    private UserService		service;
    
    @RequestMapping(value = "/activate/", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getByEmail(@RequestParam String activationToken) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "create", AuditAction.SAVE_UPDATE, SecurityContextHolder.getContext().getAuthentication(), activationToken);
	
	HttpHeaders headers = new HttpHeaders();
	headers.add("Location", System.getenv("ADS_HOST_URL"));
	User user = service.findByActivationToken(activationToken);
	
	try {
	    if (user == null) {
		throw new IllegalArgumentException("Invalid Token");
	    } else {
		user.setActive(true);
		
		// Impede que o usuario reative a propria conta novamente usando o mesmo token.
		user.setActivationToken(UUID.randomUUID().toString());
		
		service.save(user);
		logInfoMethodExit(mark, LOGGER, "activate", AuditAction.SAVE_UPDATE);
		return new ResponseEntity<byte[]>(null, headers, HttpStatus.FOUND);
	    }
	}
	catch (IllegalArgumentException e) {
	    logError(mark, LOGGER, e);
	    logInfoMethodExit(mark, LOGGER, "activate", AuditAction.SAVE_UPDATE);
	    return new ResponseEntity<byte[]>(null, headers, HttpStatus.NOT_FOUND);
	}
    }
}
