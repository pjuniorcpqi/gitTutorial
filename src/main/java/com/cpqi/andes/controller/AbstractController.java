package com.cpqi.andes.controller;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.cpqi.andes.model.Principal;
import com.cpqi.andes.model.actionenum.AuditAction;

public abstract class AbstractController {
    
    protected void logInfoMethodEntry(long mark, final Logger logger, final String methodName, final AuditAction action, final Authentication authentication,
            final Object... objs) {
	
	final StringBuilder info = new StringBuilder();

	info.append(mark);
	info.append("\nEntry method: [");
	info.append(methodName);
	info.append("] \n\t| Action: [");
	info.append(action.value());
	info.append("]\n");

	if (!(authentication instanceof AnonymousAuthenticationToken)) {
	    Principal principal = (Principal) authentication.getPrincipal();

	    info.append("\nUser info:");
	    info.append("]\n");

	}

	if (objs != null && objs.length >= 1) {
	    for (Object o : objs) {
		if (o != null) {
		    info.append("\nParam: [");
		    info.append(o.getClass());
		    info.append("] \n\t| Value: [");
		    info.append(o);
		    info.append("]");
		}
	    }
	    info.append("\n");
	}



	info.delete(0, info.length());
    }

    protected void logInfoMethodEntry(long mark, final Logger logger, final String methodName, final AuditAction action, final Principal principal) {
	
	final StringBuilder info = new StringBuilder();

	info.append(mark);

    protected void logInfoMethodEntry(long mark, final Logger logger, final String methodName, final AuditAction action, final String username) {
	
	final StringBuilder info = new StringBuilder();

	info.append(mark);
	info.append("\nEntry method: [");
	info.append(methodName);
	info.append("] \n\t| Action: [");
	info.append(action.value());
	info.append("]\n");

	if (username != null) {
	    info.append("\nUser info:");
	    info.append("\n\t| name: [");
	    info.append(username);
	    info.append("]\n");
	}

	logger.info(info.toString());

	info.delete(0, info.length());
    }

    protected void logInfoMethodExit(long mark, final Logger logger, final String methodName, final AuditAction action) {
	
	final StringBuilder info = new StringBuilder();

	info.append(mark);
	info.append("\nExit method: [");
	info.append(methodName);
	info.append("] \n\t| Action: [");
	info.append(action.value());
	info.append("]\n");

	logger.info(info.toString());

	info.delete(0, info.length());
    }

    protected void logParamInfo(long mark, final Logger logger, final String objectType, final Object obj) {
	
	final StringBuilder info = new StringBuilder();

	info.append(mark);
	info.append("\nParam: [");
	info.append(objectType);
	info.append("] \n\t| Value: [");
	info.append(obj);
	info.append("]\n");

	logger.info(info.toString());

	info.delete(0, info.length());

    }

    protected void logError(long mark, final Logger logger, final Exception whatHappend) {
	logger.error(mark, whatHappend);
    }
    
}
