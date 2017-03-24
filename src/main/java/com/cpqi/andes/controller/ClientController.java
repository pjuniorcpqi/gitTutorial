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

import com.cpqi.andes.model.Client;
import com.cpqi.andes.model.actionenum.AuditAction;
import com.cpqi.andes.service.ClientService;
import com.google.common.collect.Lists;

@RestController
public class ClientController extends AbstractController {

	private static final Logger LOGGER = Logger.getLogger(ClientController.class);

	@Autowired
	ClientService clientService;

	@RequestMapping(value = "/clients/", method = RequestMethod.GET)
	public ResponseEntity<List<Client>> listAll() {

		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "listAll", AuditAction.READ,
				SecurityContextHolder.getContext().getAuthentication());

		final List<Client> clients = Lists.newArrayList(clientService.findAll());

		if (clients.isEmpty()) {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Client>>(HttpStatus.NO_CONTENT);
		} else {
			logInfoMethodExit(mark, LOGGER, "listAll", AuditAction.READ);
			return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
		}
	}

	/**
	 * Save the client.
	 *
	 * @param client
	 *            the client
	 * @return the response entity
	 */
	@RequestMapping(value = "/clients/save", method = RequestMethod.POST)
	public ResponseEntity<Client> save(@RequestBody Client client) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "save: Client", AuditAction.SAVE_UPDATE,
				SecurityContextHolder.getContext().getAuthentication(), client);

		final Client aux = clientService.findById(client.getId());
		logParamInfo(mark, LOGGER, "Client (old)", aux);

		if (client != null && (aux == null || !aux.getName().trim().equalsIgnoreCase(client.getName().trim()))) {
			clientService.save(client);
			logInfoMethodExit(mark, LOGGER, "save: Client", AuditAction.SAVE_UPDATE);
			return new ResponseEntity<Client>(client, HttpStatus.OK);
		} else {
			logInfoMethodExit(mark, LOGGER, "save: Client", AuditAction.SAVE_UPDATE);
			return new ResponseEntity<Client>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete Client.
	 *
	 * @param Client
	 *            the Client
	 * @return the response entity
	 */
	@RequestMapping(value = "/clients/delete", method = RequestMethod.POST)
	public ResponseEntity<Client> delete(@RequestBody Client client) {
		long mark = new Date().getTime();
		logInfoMethodEntry(mark, LOGGER, "delete", AuditAction.DELETE,
				SecurityContextHolder.getContext().getAuthentication(), client);

		if (client != null) {
			clientService.delete(client);
			logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);
			return new ResponseEntity<Client>(client, HttpStatus.OK);
		} else {
			logInfoMethodExit(mark, LOGGER, "delete", AuditAction.DELETE);
			return new ResponseEntity<Client>(HttpStatus.NO_CONTENT);
		}
	}

}
