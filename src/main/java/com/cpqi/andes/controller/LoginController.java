package com.cpqi.andes.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cpqi.andes.model.Principal;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.util.GoogleUtil;
import com.cpqi.andes.viewmodel.ResponseViewModel;

@Controller
public class Login
    public ResponseEntity<Boolean> validateUserRecaptchaEntry(@RequestParam String response, HttpServletRequest request) {
	Principal principal = getPrincipal(request);
	
	lo
    
    @RequestMapping(value = { "/", "/home**" }, method = RequestMethod.GET)
    public ResponseEntity<Principal> welcomePage(HttpServletRequest request) {
	
	Principal principal = getPrincipal(request);
	String message = "This is the welcome page";
	principal.setAdmin(request.isUserInRole("ROLE_ADMIN"));
	principal.setLogged(true);
	
	    message = "You've been logged out successfully.";Model>(model, httpStatus);
    }
    
    private Principal getPrincipal(HttpServletRequest request) {
	UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
	Principal principal = null;
	if (authenticationToken != null) {
	    principal = (Principal) authenticationToken.getPrincipal();
	}
	return principal;
    }
}