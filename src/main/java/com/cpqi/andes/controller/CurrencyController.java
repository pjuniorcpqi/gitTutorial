package com.cpqi.andes.controller;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cpqi.andes.model.Currency;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.CurrencyService;
import com.cpqi.andes.viewmodel.ResponseViewModel;
import com.google.common.collect.Lists;

/**
 * <p>
 * CurrencyController
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.2
 */
@RestController
public class CurrencyController extends AbstractController {
    
    private static final Logger	LOGGER = Logger.getLogger(CurrencyController.class);
    
    @Autowired
    private CurrencyService	service;
    
    /**
     * Retrieve All Currencies.
     *
     * @return a response
     */
    @RequestMapping(value = "/currencies/", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Currency>> listAll() {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());
	
	List<Currency> curr = Lists.newArrayList(service.findAll());
	
	if (curr.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
	    return new ResponseEntity<List<Currency>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
	    return new ResponseEntity<List<Currency>>(curr, HttpStatus.OK);
	}
    }
    
    /**
     * Save a currency.
     *
     * @param code
     *            Currency code
     * @param currency
     *            Currency name
     * @return a response for what happened.
     */
    @RequestMapping(value = "/currencies/", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ResponseViewModel> save(@RequestBody Currency currency) {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "save", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(), currency);
	
	ResponseViewModel ret = new ResponseViewModel();
	try {
	    service.save(currency);
	    
	    logInfoMethodExit(mark, LOGGER, "save", AuditAction.CREATE);
	    
	    return ResponseEntity.status(HttpStatus.OK).body(ret);
	}
	catch (Exception ex) {
	    ret.setMsg(ex.getMessage());
	    
	    logError(mark, LOGGER, ex);
	    logInfoMethodExit(mark, LOGGER, "save", AuditAction.CREATE);
	    
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ret);
	}
    }
    
    /**
     * Save a currency.
     *
     * @param code
     *            Currency code
     * @param currency
     *            Currency name
     * @return a response for what happened.
     */
    @RequestMapping(value = "/currencies/{id}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ResponseViewModel> update(@RequestBody Currency currencyRequest, @PathVariable("id") long id) {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "update", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), currencyRequest, id);
	
	ResponseViewModel ret = new ResponseViewModel();
	try {
	    Currency currency = service.findById(id);
	    if (currency != null) {
		currency.setCode(currencyRequest.getCode());
		currency.setCurrency(currencyRequest.getCurrency());
		
		logParamInfo(mark, LOGGER, "Currency (new)", currency);
		
		service.save(currency);
		logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
		
		return ResponseEntity.status(HttpStatus.OK).body(ret);
	    } else {
		throw new Exception("Currency invalid.");
	    }
	}
	catch (Exception ex) {
	    ret.setMsg(ex.getMessage());
	    
	    logError(mark, LOGGER, ex);
	    logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
	    
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ret);
	}
    }
}
