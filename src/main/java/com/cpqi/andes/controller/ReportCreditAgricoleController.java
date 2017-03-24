/**
 * Copyright 2016 CPQi
 */
package com.cpqi.andes.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.configuration.DatabaseConfig;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.report.ReportFactory;

/**
 * <p>
 * ReportCreditAgricoleController
 * </p>
 * .
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @version 0.1
 * @since 0.1
 */

@RestController
public class ReportCreditAgricoleController extends AbstractController {
    
    /** The Constant LOGGER. */
    private static final Logger	LOGGER = Logger.getLogger(ReportCreditAgricoleController.class);

    /** The env. */
    @Autowired
    private Environment		env;

    /** The ser. */
    @Autowired
    private ServletContext	ser;

    /** The db conf. */
    @Autowired
    DatabaseConfig		dbConf;
    
    /**
     * Credit agricole report.
     *
     * @param map
     *            the map
     * @return the response entity
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/creditAgricoleReport", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<byte[]> creditAgricoleReport(@RequestBody HashMap<String, Object> map) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "creditAgricoleReport", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), map);

	Map<String, Object> hm = null;

	String fileName = "creditAgricoleList";

	ResponseEntity<byte[]> response = null;
	try {
	    
	    ArrayList<Long> users_ids = (ArrayList<Long>) map.get("idUsers");
	    ArrayList<Long> users_sites = (ArrayList<Long>) map.get("idSites");
	    ArrayList<Long> users_projects = (ArrayList<Long>) map.get("idProjects");
	    String monthYearSelected = (String) map.get("monthYearSelected");

	    hm = new HashMap<String, Object>();
	    hm.put("users_id", users_ids);
	    hm.put("users_sites", users_sites);
	    hm.put("users_projects", users_projects);
	    hm.put("yearMonth", monthYearSelected);
	    
	    Locale.setDefault(new Locale("pt", "BR"));
	    
	    ReportFactory factory = new ReportFactory(env);
	    
	    byte[] contents = factory
	            .reportPDF(ser.getRealPath(File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "reports" + File.separator)
	                    + File.separator + fileName, hm, dbConf.dataSource());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    response = new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);

	}
	catch (Exception e) {
	    logError(mark, LOGGER, e);
	}

	logInfoMethodExit(mark, LOGGER, "creditAgricoleReport", AuditAction.READ);
	return response;
    }

}
