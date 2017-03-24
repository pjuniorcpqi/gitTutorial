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

import com.cpqi.andes.model.IncomeType;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.IncomeTypeService;
import com.google.common.collect.Lists;

@RestController
public class IncomeTypeController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(IncomeTypeController.class);

	@Autowired
	IncomeTypeService incomeTypeService;

	@RequestMapping(value = "/incomeTypes/", method = RequestMethod.GET)
	public ResponseEntity<List<IncomeType>> listAll() {

		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		List<IncomeType> incomeTypes = Lists.newArrayList(incomeTypeService.findAll());

		if (incomeTypes.isEmpty()) {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<IncomeType>>(HttpStatus.NO_CONTENT);
		} else {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<IncomeType>>(incomeTypes, HttpStatus.OK);
		}

	}

}
