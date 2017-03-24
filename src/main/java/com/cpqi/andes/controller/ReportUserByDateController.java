package com.cpqi.andes.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import com.cpqi.andes.dto.ReportUserByDateDTO;
import com.cpqi.andes.model.Allocation;
import com.cpqi.andes.model.Client;
import com.cpqi.andes.model.Phase;
import com.cpqi.andes.model.Profile;
import com.cpqi.andes.model.Project;
import com.cpqi.andes.model.ReportFilterUserByDate;
import com.cpqi.andes.model.ReportUserByDate;
import com.cpqi.andes.model.Site;
import com.cpqi.andes.model.TimeLog;
import com.cpqi.andes.model.User;
import com.cpqi.andes.model.WorkLog;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.ClientService;
import com.cpqi.andes.service.PhaseService;
import com.cpqi.andes.service.ProjectService;
import com.cpqi.andes.service.SiteService;
import com.cpqi.andes.service.TimeLogService;
import com.cpqi.andes.service.UserService;
import com.cpqi.andes.service.WorkLogService;
import com.cpqi.andes.viewmodel.ReportAllocationViewModel;
import com.cpqi.andes.viewmodel.ReportUserByDateViewModel;
import com.google.common.collect.Lists;

/**
 * The Class ReportUserByDateController.
 */
@RestController
public class ReportUserByDateController extends AbstractController {

	private static final Logger LOGGER = Logger
			.getLogger(ReportUserByDateController.class);

	/** The project service. */
	@Autowired
	ProjectService projectService;

	/** The user service. */
	@Autowired
	UserService userService;

	/** The time log service. */
	@Autowired
	TimeLogService timeLogService;

	/** The work log service. */
	@Autowired
	WorkLogService workLogService;

	@Autowired
	PhaseService phaseService;

	/** The client service. */
	@Autowired
	ClientService clientService;

	@Autowired
	SiteService siteService;

	/**
	 * List all.
	 *
	 * @return the response entity
	 */
	@RequestMapping(value = "report/loadFilter")
	public ResponseEntity<ReportUserByDate> listAll() {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		List<Project> projects = Lists.newArrayList(projectService.findAll());
		List<User> users = Lists.newArrayList(userService.findAll());
		List<Client> clients = Lists.newArrayList(clientService.findAll());
		List<Phase> phases = Lists.newArrayList(phaseService.findAll());

		List<Site> sites = Lists.newArrayList(siteService.findAll());

		ReportUserByDate reportUserByDate = new ReportUserByDate();
		reportUserByDate.setProjects(projects);
		reportUserByDate.setUsers(users);
		reportUserByDate.setPhases(phases);
		reportUserByDate.setClients(clients);
		reportUserByDate.setSites(sites);

		if (projects.isEmpty() || users.isEmpty()) {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<ReportUserByDate>(HttpStatus.NO_CONTENT);
		} else {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<ReportUserByDate>(reportUserByDate,
					HttpStatus.OK);
		}

	}

	/**
	 * Search.
	 *
	 * @param reportFilter
	 *            the report filter
	 * @return the response entity
	 */
	@RequestMapping(value = "report/search", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<ReportUserByDateViewModel>> search(
			@RequestBody ReportFilterUserByDate reportFilter) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "search", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication(),
				reportFilter);

		ArrayList<ReportUserByDateViewModel> viewModel = new ArrayList<ReportUserByDateViewModel>();

		if (reportFilter == null) {
			logInfoMethodExit(mark, LOGGER, "search", AuditAction.READ);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(viewModel);
		}
		if (reportFilter.getEndDate() == null) {
			reportFilter.setEndDate(Calendar.getInstance());
		}
		if (reportFilter.getClientId() == null) {
			reportFilter.setClientId(Arrays.asList(0L));
		}
		if (reportFilter.getPhaseId() == null) {
			reportFilter.setPhaseId(Arrays.asList(0L));
		}
		if (reportFilter.getProjectId() == null) {
			reportFilter.setProjectId(Arrays.asList(0L));
		}
		if (reportFilter.getUserId() == null) {
			reportFilter.setUserId(Arrays.asList(0L));
		}

		ArrayList<Object> resultSelect = timeLogService.filterReportUserHours(
				reportFilter.getUserId(), reportFilter.getStartDate(),
				reportFilter.getEndDate(), reportFilter.getProjectId(),
				reportFilter.getClientId(), reportFilter.getPhaseId());

		if (resultSelect.size() > 0) {

			ArrayList<ReportUserByDateDTO> entries = buildDTO(resultSelect);

			ReportUserByDateViewModel tempVM = null;
			HashMap<Long, ReportAllocationViewModel> mapAllocations = null;
			Calendar lastDate = null;
			for (ReportUserByDateDTO dto : entries) {

				/*
				 * Se o usuario for diferente (id diferente), entao todos os
				 * tempos do usuario anterior ja foram calculados. Isso pode ser
				 * assumido devido ao resultSelect ser ordenado por id e data.
				 */
				if (tempVM == null
						|| tempVM.getUserId() != dto.getTimeLog().getUser()
								.getId()) {

					/*
					 * Caso o usuario tenha trabalhado algum tempo (pelo menos
					 * um par de inTime e outTime), essas
					 * informações(identificação do usuario, total de horas
					 * trabalhadas e em que alocações elas foram divididas)
					 * devem ser inseridas no objeto que será enviado para o
					 * front (viewModel).
					 */
					if (tempVM != null
							&& tempVM.getTotalHours() != BigDecimal.ZERO) {
						tempVM.setAllocations(mapAllocations);
						viewModel.add(tempVM);
					}

					lastDate = null;
					tempVM = new ReportUserByDateViewModel();
					mapAllocations = new HashMap<Long, ReportAllocationViewModel>();
					tempVM.setUserName(dto.getTimeLog().getUser().getName());
					tempVM.setUserId(dto.getTimeLog().getUser().getId());
				}

				/*
				 * Isso evita que os timelogs de um mesmo dia sejam somandos
				 * repetidamente, caso o usuario possua mais de uma alocação
				 * por dia.
				 */
				if (!dto.getTimeLog().getDate().equals(lastDate)) {
					tempVM.setTotalHours(tempVM
							.getTotalHours()
							.add(calculeTime(dto.getTimeLog().getInTime1(), dto
									.getTimeLog().getOutTime1()))
							.add(calculeTime(dto.getTimeLog().getInTime2(), dto
									.getTimeLog().getOutTime2()))
							.add(calculeTime(dto.getTimeLog().getInTime3(), dto
									.getTimeLog().getOutTime3())));
				}

				if (dto.getWorkLog() != null) {
					ReportAllocationViewModel allocation = null;

					if (mapAllocations.containsKey(dto.getWorkLog()
							.getAllocation().getId())) {
						allocation = mapAllocations.get(dto.getWorkLog()
								.getAllocation().getId());
						allocation
								.setTotalHours(allocation
										.getTotalHours()
										.add(dto.getWorkLog().getTimeInserted() != null ? dto
												.getWorkLog().getTimeInserted()
												: BigDecimal.ZERO));
					} else {
						allocation = new ReportAllocationViewModel();
						allocation.setPhaseName(dto.getWorkLog()
								.getAllocation().getPhase().getDescription());
						allocation.setAllocationName(dto.getWorkLog()
								.getAllocation().getPhase().getProject()
								.getTitle());
						allocation.setProfileName(dto.getWorkLog()
								.getAllocation().getProfile().getDescription());
						allocation.setClientName(dto.getWorkLog()
								.getAllocation().getPhase().getProject()
								.getClient().getName());
						allocation.setTotalHours(dto.getWorkLog()
								.getTimeInserted());
						if (allocation.getTotalHours() != null) {
							mapAllocations.put(dto.getWorkLog().getAllocation()
									.getId(), allocation);
						}
					}
				}

				lastDate = dto.getTimeLog().getDate();
			}

			tempVM.setAllocations(mapAllocations);
			viewModel.add(tempVM);
		}

		logInfoMethodExit(mark, LOGGER, "search", AuditAction.READ);
		return ResponseEntity.status(HttpStatus.OK).body(viewModel);
	}

	/**
	 * Calcule time.
	 *
	 * @param inS
	 *            the in s
	 * @param outS
	 *            the out s
	 * @return the big decimal
	 */
	private BigDecimal calculeTime(String inS, String outS) {

		try {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");

			Date out = outS != null ? format.parse(outS) : null;
			Date in = inS != null ? format.parse(inS) : null;

			if (out != null && in != null) {
				BigDecimal diff = new BigDecimal(out.getTime() - in.getTime());
				diff = diff.divide(BigDecimal.valueOf(1000)); // Convert s
				diff = diff.divide(BigDecimal.valueOf(60)); // Convert m
				diff = diff.divide(BigDecimal.valueOf(60), 2,
						RoundingMode.HALF_EVEN); // Convert h
				return diff;
			}

		} catch (ParseException e) {

		}

		return BigDecimal.ZERO;
	}

	/**
	 * Builds the dto.
	 *
	 * @param resultSelect
	 *            the result select
	 * @return the array list
	 */
	private ArrayList<ReportUserByDateDTO> buildDTO(
			ArrayList<Object> resultSelect) {

		ArrayList<ReportUserByDateDTO> result = new ArrayList<>();

		for (int i = 0; i < resultSelect.size(); i++) {

			Object[] tupla = (Object[]) resultSelect.get(i);
			Calendar aux = Calendar.getInstance();

			TimeLog t = new TimeLog();
			User user = new User();
			user.setId(((BigDecimal) tupla[0]).longValue());
			user.setName((String) tupla[1]);
			t.setUser(user);

			if (tupla[2] != null) {
				aux.setTimeInMillis(((Timestamp) tupla[2]).getTime());
			} else {
				aux = null;
			}

			t.setDate(aux);
			t.setInTime1((String) tupla[3]);
			t.setOutTime1((String) tupla[4]);
			t.setInTime2((String) tupla[5]);
			t.setOutTime2((String) tupla[6]);
			t.setInTime3((String) tupla[7]);
			t.setOutTime3((String) tupla[8]);

			WorkLog w = new WorkLog();
			Allocation allocation = new Allocation();
			Project project = new Project();
			project.setTitle((String) tupla[9]);
			Client client = new Client();
			client.setName((String) tupla[10]);
			project.setClient(client);
			Phase phase = new Phase();
			phase.setProject(project);
			phase.setDescription((String) tupla[11]);
			allocation.setPhase(phase);
			Profile profile = new Profile();
			profile.setDescription((String) tupla[12]);
			allocation.setProfile(profile);
			allocation.setId(((BigDecimal) tupla[13]).longValue());
			w.setAllocation(allocation);

			if (tupla[14] != null) {
				aux = Calendar.getInstance();
				aux.setTimeInMillis(((Timestamp) tupla[14]).getTime());
			} else {
				aux = null;
			}

			w.setDate(aux);
			w.setTimeInserted((BigDecimal) tupla[15]);

			result.add(new ReportUserByDateDTO(t, w));
		}

		return result;
	}
}
