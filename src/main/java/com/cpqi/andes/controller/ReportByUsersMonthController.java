package com.cpqi.andes.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.configuration.DatabaseConfig;
import com.cpqi.andes.model.Principal;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.report.ReportFactory;

/**
 * The Class ReportUserByDateController.
 */
@RestController
public class ReportByUsersMonthController extends AbstractController {
    
    @Autowired
    private Environment		env;
    
    @Autowired
    private ServletContext	ser;
    
    @Autowired
    DatabaseConfig		dbConf;
    
    private static final Logger	LOGGER = Logger.getLogger(ReportByUsersMonthController.class);
    
    @RequestMapping(value = "report/monthReportsTimeSheet", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<byte[]> monthReportsTimeSheet(@RequestBody HashMap<String, Object> map) {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "monthReportsTimeSheet", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), map);
	
	Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
	Principal principal = null;
	if (!(userAuthentication instanceof AnonymousAuthenticationToken)) {
	    principal = (Principal) userAuthentication.getPrincipal();
	}
	
	String username = principal != null ? principal.getUsername() : "Username";
	
	Map<String, Object> hm = null;
	
	String fileName = "usersListTimeSheet";
	
	ResponseEntity<byte[]> response = null;
	try {
	    ArrayList<Long> users_Ids = (ArrayList<Long>) map.get("idUsers");
	    ArrayList<Long> users_sites = (ArrayList<Long>) map.get("idSites");
	    ArrayList<Long> users_projects = (ArrayList<Long>) map.get("idProjects");
	    String monthYearSelected = (String) map.get("monthYearSelected");
	    
	    hm = new HashMap<String, Object>();
	    
	    if (users_projects == null || users_projects.isEmpty()) {
		hm.put("allProjects", "T");
	    } else {
		hm.put("allProjects", "");
	    }
	    
	    hm.put("users_id", users_Ids);
	    hm.put("users_sites", users_sites);
	    hm.put("users_projects", users_projects);
	    hm.put("yearMonth", monthYearSelected);
	    
	    hm.put("requesterName", username);
	    
	    ReportFactory factory = new ReportFactory(env);
	    
	    byte[] contents = factory
	            .reportPDF(ser.getRealPath(File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "reports" + File.separator)
	                    + File.separator + fileName, hm, dbConf.dataSource());
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    
	    headers.setContentDispositionFormData("attachment", username + ".pdf");
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	    
	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	}
	
	logInfoMethodExit(mark, LOGGER, "userReportTimeSheet", AuditAction.READ);
	return response;
    }
}
