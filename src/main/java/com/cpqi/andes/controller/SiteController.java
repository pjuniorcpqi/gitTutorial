package com.cpqi.andes.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.Site;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.SiteService;
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
public class SiteController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(SiteController.class);

	/** The service. */
	@Autowired
	private SiteService service;

	/**
	 * List all sites.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/sites/", method = RequestMethod.GET)
	public ResponseEntity<List<Site>> listAll() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		List<Site> sites = Lists.newArrayList(service.findAll());

		if (sites.isEmpty()) {

			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Site>>(HttpStatus.NO_CONTENT);

		} else {

			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Site>>(sites, HttpStatus.OK);

		}
	}

	/**
	 * Save the site.
	 *
	 * @param site
	 *            the site
	 * @return the response entity
	 */
	@RequestMapping(value = "/sites/save", method = RequestMethod.POST)
	public ResponseEntity<Site> save(@RequestBody Site site) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "save", AuditAction.SAVE_UPDATE,
				SecurityContextHolder.getContext().getAuthentication(), site);

		Site aux = service.findById(site.getId());

		if (site != null && (aux == null || !aux.getName().trim().equalsIgnoreCase(site.getName().trim()))) {

			logParamInfo(mark, LOGGER, "Site (old)", aux);
			service.save(site);

			logInfoMethodExit(mark, LOGGER, "save", AuditAction.SAVE_UPDATE);
			return new ResponseEntity<Site>(site, HttpStatus.OK);

		} else {

			logInfoMethodExit(mark, LOGGER, "save", AuditAction.SAVE_UPDATE);
			return new ResponseEntity<Site>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete site.
	 *
	 * @param site
	 *            the site
	 * @return the response entity
	 */
	@RequestMapping(value = "/sites/delete", method = RequestMethod.POST)
	public ResponseEntity<Site> delete(@RequestBody Site site) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "delete", AuditAction.DELETE,
				SecurityContextHolder.getContext().getAuthentication(), site);

		if (site != null) {

			service.delete(site);

			logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);
			return new ResponseEntity<Site>(site, HttpStatus.OK);
		} else {
			logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);
			return new ResponseEntity<Site>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Delete all sites.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/sites/clear")
	public ResponseEntity<Site> deleteAll() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "deleteAll", AuditAction.DELETE,
				SecurityContextHolder.getContext().getAuthentication());

		service.deleteAll();

		logInfoMethodExit(mark, LOGGER, "deleteAll", AuditAction.DELETE);
		return new ResponseEntity<Site>(HttpStatus.NO_CONTENT);
	}
}
