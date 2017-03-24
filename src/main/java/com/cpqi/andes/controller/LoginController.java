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
public class LoginController extends AbstractController {
    
    private static final Logger LOGGER = Logger.getLogger(LoginController.class);
    
    @RequestMapping(value = { "/recaptcha" }, method = RequestMethod.POST)
    public ResponseEntity<Boolean> validateUserRecaptchaEntry(@RequestParam String response, HttpServletRequest request) {
	Principal principal = getPrincipal(request);
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "validateUserRecaptchaEntry", AuditAction.READ, principal);
	
	boolean isReCaptchaResponseOk = false;
	try {
	    isReCaptchaResponseOk = GoogleUtil.verifyReCaptcha(request, response);
	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	}
	
	ResponseEntity<Boolean> result = new ResponseEntity<Boolean>(isReCaptchaResponseOk, HttpStatus.OK);
	logInfoMethodExit(mark, LOGGER, "welcomePage", AuditAction.READ);
	return result;
    }
    
    @RequestMapping(value = { "/", "/home**" }, method = RequestMethod.GET)
    public ResponseEntity<Principal> welcomePage(HttpServletRequest request) {
	
	Principal principal = getPrincipal(request);
	String message = "This is the welcome page";
	principal.setAdmin(request.isUserInRole("ROLE_ADMIN"));
	principal.setLogged(true);
	principal.setMessage(message);
	
	long mark = new Date().getTime();
	
	ResponseEntity<Principal> responseEntity = new ResponseEntity<Principal>(principal, HttpStatus.OK);
	
	logInfoMethodExit(mark, LOGGER, "welcomePage", AuditAction.READ);
	return responseEntity;
	
    }
    
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public ResponseEntity<Void> acessDeniedPage(HttpServletRequest request, HttpServletResponse response) {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "acessDeniedPage", AuditAction.READ, getPrincipal(request));
	
	HttpHeaders headers = new HttpHeaders();
	HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
	UriComponentsBuilder b = UriComponentsBuilder.fromPath("/#/403");
	UriComponents uriComponents = b.build();
	headers.setLocation(uriComponents.toUri());
	
	logInfoMethodExit(mark, LOGGER, "acessDeniedPage", AuditAction.READ);
	
	return new ResponseEntity<Void>(headers, httpStatus);
    }
    
    // Spring Security see this :
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<ResponseViewModel> login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {
	
	String username = request.getHeader("username");
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "login/logout", AuditAction.READ, username);
	
	ResponseViewModel model = new ResponseViewModel();
	HttpStatus httpStatus = HttpStatus.OK;
	String message = null;
	
	AuthenticationException exception = (AuthenticationException) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	
	if (exception != null) {
	    message = exception.getMessage();
	}
	
	if (logout != null) {
	    StringBuilder info = new StringBuilder();
	    if (username != null) {
		info.append(username);
	    }
	    info.append(". ");
	    
	    message = "You've been logged out successfully.";
	    info.append(message);
	    
	    LOGGER.info(info.toString());
	}
	if (message == null) {
	    httpStatus = HttpStatus.NETWORK_AUTHENTICATION_REQUIRED;
	} else {
	    model.setMsg(message);
	}
	
	logInfoMethodExit(mark, LOGGER, "login/logout", AuditAction.READ);
	return new ResponseEntity<ResponseViewModel>(model, httpStatus);
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