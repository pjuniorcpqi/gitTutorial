package com.cpqi.andes.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.Settings;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.SettingsService;

/**
 * <p>
 * SettingsController
 * </p>
 * .
 *
 * @author Tiago Morano <tmorano@cpqi.com>
 * @version 0.1
 * @since 0.1
 */

@RestController
public class SettingsController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(SettingsController.class);

	@Autowired
	SettingsService settingsService;

	/**
	 * Get default View Mode
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "/settingsValues", method = RequestMethod.GET)
	public ResponseEntity<Settings> listAllSettings() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAllSettings", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		Settings defaultViewMode = settingsService.findById(1);

		if (defaultViewMode != null) {

			logInfoMethodExit(mark, LOGGER, "listAllSettings", AuditAction.READ);
			return new ResponseEntity<Settings>(defaultViewMode, HttpStatus.OK);

		} else {

			logInfoMethodExit(mark, LOGGER, "listAllSettings", AuditAction.READ);
			return new ResponseEntity<Settings>(HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Save the settings update.
	 *
	 * @param site
	 *            the site
	 * @return the response entity
	 */
	@RequestMapping(value = "/settings/save", method = RequestMethod.POST)
	public ResponseEntity<Settings> save(@RequestBody Settings settings) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "save", AuditAction.SAVE_UPDATE,
				SecurityContextHolder.getContext().getAuthentication(), settings);

		Settings aux = settingsService.findById(settings.getId());

		if (settings != null && (aux == null || aux.getId() == settings.getId())) {

			logParamInfo(mark, LOGGER, "Settings (old)", aux);
			settingsService.save(settings);

			logInfoMethodExit(mark, LOGGER, "save", AuditAction.SAVE_UPDATE);
			return new ResponseEntity<Settings>(settings, HttpStatus.OK);

		} else {

			logInfoMethodExit(mark, LOGGER, "save", AuditAction.SAVE_UPDATE);
			return new ResponseEntity<Settings>(HttpStatus.BAD_REQUEST);

		}
	}

}
