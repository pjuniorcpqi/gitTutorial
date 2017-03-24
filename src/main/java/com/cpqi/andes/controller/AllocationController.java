package com.cpqi.andes.controller;

import java.util.ArrayList;
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

import com.cpqi.andes.model.Allocation;
import com.cpqi.andes.model.AllocationsRequest;
import com.cpqi.andes.model.Client;
import com.cpqi.andes.model.Phase;
import com.cpqi.andes.model.Profile;
import com.cpqi.andes.model.Project;
import com.cpqi.andes.model.User;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.AllocationService;
import com.cpqi.andes.service.ClientService;
import com.cpqi.andes.service.PhaseService;
import com.cpqi.andes.service.ProfileService;
import com.cpqi.andes.service.ProjectService;
import com.cpqi.andes.service.UserService;
import com.cpqi.andes.service.WorkLogService;
import com.cpqi.andes.viewmodel.ResponseViewModel;
import com.google.common.collect.Lists;

/**
 * <p>
 * AllocationController
 * </p>
 *
 * @author Pedro Marcos <pjunior@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@RestController
public class AllocationController extends AbstractController {
    
    private static final Logger	LOGGER = Logger.getLogger(AllocationController.class);

    @Autowired
    private AllocationService	service;

    @Autowired
    private PhaseService	phaseService;

    @Autowired
    private ProfileService	profileService;

    @Autowired
    private UserService		userService;

    @Autowired
    private ClientService	clientService;

    @Autowired
    private ProjectService	projectService;

    @Autowired
    private WorkLogService	workLogService;

    /**
     * Populates fields from an Allocation view Request.
     *
     * @return AllocationsRequest.
     */
    @RequestMapping(value = "allocations/populate")
    public ResponseEntity<AllocationsRequest> populateFields() {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "populateFields", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	AllocationsRequest allocationsRequest = new AllocationsRequest();
	List<Phase> phases = Lists.newArrayList(phaseService.findAll());
	List<Profile> profiles = Lists.newArrayList(profileService.findAll());
	List<Client> clients = Lists.newArrayList(clientService.findAll());
	List<Project> projects = Lists.newArrayList(projectService.findAll());
	allocationsRequest.setPhases(phases);
	allocationsRequest.setProfiles(profiles);
	allocationsRequest.setClients(clients);
	allocationsRequest.setProjects(projects);

	logInfoMethodExit(mark, LOGGER, "populateFields", AuditAction.READ);
	return new ResponseEntity<AllocationsRequest>(allocationsRequest, HttpStatus.OK);

    }

    /**
     * Retrieves all allocations.
     *
     * @return List of Allocation.
     */
    @RequestMapping(value = "allocations/all")
    public ResponseEntity<List<Allocation>> findAll() {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "findAll: List<Allocation>", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	ArrayList<Allocation> c = Lists.newArrayList(service.findAll());
	if (c != null) {
	    logInfoMethodExit(mark, LOGGER, "findAll: List<Allocation>", AuditAction.READ);
	    return new ResponseEntity<List<Allocation>>(c, HttpStatus.OK);
	} else {
	    logInfoMethodExit(mark, LOGGER, "findAll: List<Allocation>", AuditAction.READ);
	    return new ResponseEntity<List<Allocation>>(HttpStatus.NO_CONTENT);
	}
    }

    /**
     * Retrieves allocations for a given user.
     *
     * @param id
     *            User id
     * @return List of Allocation.
     */
    @RequestMapping(value = "/allocations", method = RequestMethod.POST)
    public ResponseEntity<List<Allocation>> findByUser(@RequestBody int id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "findByUser: List<Allocation>", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication(), id);

	ArrayList<Allocation> c = service.findByUserId(id);

	if (c != null) {
	    logInfoMethodExit(mark, LOGGER, "findByUser: List<Allocation>", AuditAction.READ);
	    return new ResponseEntity<List<Allocation>>(c, HttpStatus.OK);
	} else {
	    logInfoMethodExit(mark, LOGGER, "findByUser: List<Allocation>", AuditAction.READ);
	    return new ResponseEntity<List<Allocation>>(HttpStatus.NO_CONTENT);
	}
    }

    /**
     * Saves an Allocation
     *
     * @param allocation
     * @return
     */
    @RequestMapping(value = "allocations/save/", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Allocation> save(@RequestBody Allocation allocation) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "save: Allocation", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(), allocation);

	allocation.setUser(null);
	service.save(allocation);

	logInfoMethodExit(mark, LOGGER, "save: Allocation", AuditAction.CREATE);
	return new ResponseEntity<Allocation>(allocation, HttpStatus.NO_CONTENT);
    }

    /**
     * Update a Phase
     *
     * @param Phase
     *            phase
     * @return
     */
    @RequestMapping(value = "allocations/save/{id}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ResponseViewModel> update(@RequestBody Allocation allocation, @PathVariable("id") long id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "update: Allocation", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), allocation, id);

	ResponseViewModel ret = new ResponseViewModel();
	if (allocation != null) {
	    try {
		logParamInfo(mark, LOGGER, "Allocation (new)", allocation);
		service.save(allocation);
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());
		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "update: Allocation", AuditAction.UPDATE);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }

	    logInfoMethodExit(mark, LOGGER, "update: Allocation", AuditAction.UPDATE);
	    return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	} else {
	    ret.setMsg("Allocation not exists");
	    logInfoMethodExit(mark, LOGGER, "update: Allocation", AuditAction.UPDATE);
	    return ResponseEntity.badRequest().body(ret);
	}
    }

    /**
     * @param allocation
     * @return
     */
    @RequestMapping(value = "allocations/remove/{id}", method = RequestMethod.POST)
    public ResponseEntity<ResponseViewModel> remove(@PathVariable("id") long id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "remove", AuditAction.DELETE, SecurityContextHolder.getContext().getAuthentication(), id);

	ResponseViewModel ret = new ResponseViewModel();
	try {
	    Allocation allocation2 = service.findById(id);

	    if (allocation2 != null) {
		if (allocation2.getUser() != null) {
		    throw new Exception("This allocation has an allocated User  : Please! Deallocate this User");
		} else {
		    service.delete(id);
		}
	    }

	}
	catch (Exception ex) {
	    ret.setMsg(ex.getMessage());
	    logError(mark, LOGGER, ex);
	    logInfoMethodExit(mark, LOGGER, "remove", AuditAction.DELETE);
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	}

	logInfoMethodExit(mark, LOGGER, "remove", AuditAction.DELETE);
	return ResponseEntity.status(HttpStatus.CREATED).body(ret);
    }

    /**
     * Retrieves all allocations with no linked User.
     *
     * @return List of Allocation.
     */
    @RequestMapping(value = "allocations/noUser")
    public ResponseEntity<List<Allocation>> findAllWithNoUser() {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "findAllWithNoUser: List<Allocation>", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	ArrayList<Allocation> c = Lists.newArrayList(service.findAllAllocationsWithNoUser());
	if (c != null) {
	    logInfoMethodExit(mark, LOGGER, "findAllWithNoUser: List<Allocation>", AuditAction.READ);
	    return new ResponseEntity<List<Allocation>>(c, HttpStatus.OK);
	} else {
	    logInfoMethodExit(mark, LOGGER, "findAllWithNoUser: List<Allocation>", AuditAction.READ);
	    return new ResponseEntity<List<Allocation>>(HttpStatus.NO_CONTENT);
	}
    }

    /**
     * Saves allocations linked to a User.
     *
     * @param allocations
     *            List of Allocation
     * @param id
     *            User id
     */
    @RequestMapping(value = "/allocations/allocate/{id}", method = RequestMethod.POST)
    public ResponseEntity<Allocation> allocateUser(@RequestBody List<Allocation> allocations, @PathVariable("id") long id) {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "allocateUser: Allocation", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), allocations,
	        id);

	User user = userService.findById(id);
	for (Allocation alloc : allocations) {
	    alloc.setUser(user);
	    service.save(alloc);
	}

	logInfoMethodExit(mark, LOGGER, "allocateUser: Allocation", AuditAction.UPDATE);
	return new ResponseEntity<Allocation>(HttpStatus.NO_CONTENT);
    }

    /**
     * Saves allocations linked with no User, deallocating the User.
     *
     * @param allocations
     *            List of Allocation
     */
    @RequestMapping(value = "/allocations/deallocate", method = RequestMethod.POST)
    public ResponseEntity<Allocation> deallocateUser(@RequestBody List<Allocation> allocations) {
	
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "deallocateUser: Allocation", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), allocations);

	for (Allocation alloc : allocations) {
	    alloc.setUser(null);
	    service.save(alloc);
	}

	logInfoMethodExit(mark, LOGGER, "deallocateUser: Allocation", AuditAction.UPDATE);
	return new ResponseEntity<Allocation>(HttpStatus.NO_CONTENT);
    }
}
