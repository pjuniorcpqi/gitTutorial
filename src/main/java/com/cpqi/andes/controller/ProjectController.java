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

import com.cpqi.andes.model.Project;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.ProjectService;
import com.cpqi.andes.viewmodel.ResponseViewModel;
import com.google.common.collect.Lists;

@RestController
public class ProjectController extends AbstractController {
    
    private static final Logger	LOGGER = Logger.getLogger(ProjectController.class);

    @Autowired
    ProjectService		projectService;

    @RequestMapping(value = "/project/save")
    public ResponseEntity<Project> saveNew(@RequestBody Project project) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "saveNew", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(), project);

	projectService.save(project);

	logInfoMethodExit(mark, LOGGER, "saveNew", AuditAction.CREATE);
	return new ResponseEntity<Project>(project, HttpStatus.NO_CONTENT);

    }

    @RequestMapping(value = "/projects")
    public ResponseEntity<List<Project>> listAll() {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ, SecurityContextHolder.getContext().getAuthentication());

	List<Project> projects = Lists.newArrayList(projectService.findAll());

	if (projects.isEmpty()) {
	    logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
	    return new ResponseEntity<List<Project>>(HttpStatus.NO_CONTENT);
	} else {
	    logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
	    return new ResponseEntity<List<Project>>(projects, HttpStatus.OK);
	}

    }

    @RequestMapping(value = "/project/delete")
    public ResponseEntity<Project> delete(@RequestBody Project project) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "delete", AuditAction.DELETE, SecurityContextHolder.getContext().getAuthentication(), project);

	projectService.delete(project);

	logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);
	return new ResponseEntity<Project>(project, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "project/{id}", method = RequestMethod.POST)
    public ResponseEntity<ResponseViewModel> update(@RequestBody Project requestedProject, @PathVariable("id") long id) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "update", AuditAction.UPDATE, SecurityContextHolder.getContext().getAuthentication(), requestedProject, id);

	ResponseViewModel ret = new ResponseViewModel();
	if (requestedProject != null) {
	    try {
		Project project = projectService.findById(requestedProject.getId());
		if (project != null) {
		    if (!requestedProject.getTitle().equalsIgnoreCase(project.getTitle())) {
			validateName(requestedProject);

		    }

		    logParamInfo(mark, LOGGER, "Project (old)", project);

		    project.setTitle(requestedProject.getTitle());
		    project.setClient(requestedProject.getClient());
		    project.setProjectType(requestedProject.getProjectType());
		    project.setInternal(requestedProject.isInternal());
		    project.setIncomeType(requestedProject.getIncomeType());
		    project.setCurrency(requestedProject.getCurrency());
		    project.setDescription(requestedProject.getDescription());
		    project.setStartDate(requestedProject.getStartDate());
		    project.setEndDate(requestedProject.getEndDate());
		    projectService.save(project);

		} else {
		    throw new Exception("Project does not exist");
		}

		logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
		return ResponseEntity.status(HttpStatus.CREATED).body(ret);
	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());

		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }
	} else {
	    ret.setMsg("Project does not exist");
	    LOGGER.info("Project does not exist");
	}

	logInfoMethodExit(mark, LOGGER, "update", AuditAction.UPDATE);
	return ResponseEntity.badRequest().body(ret);
    }

    private void validateName(Project requestedProject) throws Exception {
	List<Project> projects = Lists.newArrayList(projectService.findAll());
	for (Project eachProject : projects) {
	    if (requestedProject.getTitle().trim().equalsIgnoreCase(eachProject.getTitle().trim())) {
		throw new Exception("Project Already Exists");
	    }
	}
    }

    @RequestMapping(value = "project/", method = RequestMethod.POST)
    public ResponseEntity<ResponseViewModel> create(@RequestBody Project newProject) {
	long mark = new Date().getTime();
	logInfoMethodEntry(mark, LOGGER, "create", AuditAction.CREATE, SecurityContextHolder.getContext().getAuthentication(), newProject);

	ResponseViewModel ret = new ResponseViewModel();
	if (newProject != null) {
	    try {
		validateName(newProject);

		projectService.save(newProject);

		logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);
		return ResponseEntity.status(HttpStatus.CREATED).body(ret);

	    }
	    catch (Exception ex) {
		ret.setMsg(ex.getMessage());

		logError(mark, LOGGER, ex);
		logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ret);
	    }
	} else {
	    ret.setMsg("Project does not exist");
	    LOGGER.info("Project does not exist");
	}

	logInfoMethodExit(mark, LOGGER, "create", AuditAction.CREATE);
	return ResponseEntity.badRequest().body(ret);
    }
}
