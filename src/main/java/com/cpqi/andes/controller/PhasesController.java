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

import com.cpqi.andes.model.Phase;
import com.cpqi.andes.model.Project;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.PhaseService;
import com.cpqi.andes.service.ProjectService;
import com.cpqi.andes.viewmodel.ResponseViewModel;
import com.google.common.collect.Lists;

@RestController
public class PhasesController extends AbstractController {
    
    private static final Logger	LOGGER = Logger.getLogger(PhasesController.class);

    @Autowired
    private PhaseService	phaseService;

    @Autowired
    private ProjectService	projectService;

    /**
     * Retrieve All Users.
     *
     * @return a response
     */
    @RequestMapping(value = "phases/all")
    public ResponseEntity<List<Phase>> getAll() {
	long mark = new Date().getTime();

	logInfoMethodEntry(mark, LOGGER, "getAll", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	List<Phase> phases = Lists.newArrayList(phaseService.findAll());
	if (phases.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "getAll", AuditAction.READ);
	    return new ResponseEntity<List<Phase>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "getAll", AuditAction.READ);
	    return new ResponseEntity<List<Phase>>(phases, HttpStatus.OK);
	}
    }

    @RequestMapping(value = "phases/allProjects")
    public ResponseEntity<List<Project>> listAllProject() {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "listAllProject", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	List<Project> projects = Lists.newArrayList(projectService.findAll());
	if (projects.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "listAllProject", AuditAction.READ);
	    return new ResponseEntity<List<Project>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "listAllProject", AuditAction.READ);
	    return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}
    }

    /**
     * Create a Phase
     *
     * @param Phase
     * @return
     */
    @RequestMapping(value = "phase/", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ResponseViewModel> create(@RequestBody Phase phase) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "create", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(), phase);

	ResponseViewModel ret = new ResponseViewModel();
	if (phase != null) {
	    try {
		Phase hasPhase = phaseService.findByDescription(phase.getDescription().trim());
		if (hasPhase == null) {
		    phaseService.save(phase);
		} else {
		    throw new Exception("Description already Phases.");
		}
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());

		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }

	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    ret.setMsg("Invalid data");

	    logError(mark, LOGGER, new Exception("Invalid Data"));
	    logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);

	    return ResponseEntity.badRequest().body(ret);
	}
    }

    /**
     * Update a Phase
     *
     * @param Phase
     *            phase
     * @return
     */
    @RequestMapping(value = "phase/{id}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ResponseViewModel> update(@RequestBody Phase phase, @PathVariable("id") long id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "update", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), phase, id);

	ResponseViewModel ret = new ResponseViewModel();
	if (phase != null) {
	    try {
		Phase hasPhase = phaseService.findById(id);
		if (hasPhase != null) {
		    phaseService.save(phase);
		} else {
		    throw new Exception("Phase does not exists.");
		}
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());

		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }

	    logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    ret.setMsg("Phase not exists");

	    LOGGER.info("Phase not exists");
	    logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);

	    return ResponseEntity.badRequest().body(ret);
	}
    }

    @RequestMapping(value = "phase/delete/{id}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ResponseViewModel> delete(@RequestBody Phase phase, @PathVariable("id") long id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "delete", AuditAction.DELETE, SecurityContextHolder.getContext().getAuthentication(), phase, id);

	ResponseViewModel ret = new ResponseViewModel();
	if (phase != null) {
	    try {
		Phase hasPhase = phaseService.findById(id);
		if (hasPhase != null) {
		    phaseService.delete(phase);
		} else {
		    throw new Exception("Phase does not exists.");
		}
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());

		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }

	    logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);
	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    ret.setMsg("Phase not exists");

	    LOGGER.info("Phase not exists");
	    logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);

	    return ResponseEntity.badRequest().body(ret);
	}
    }

}
