package com.cpqi.andes.model;

import java.util.List;

public class ReportUserByDate {

	private List<Project> projects;

	private List<User> users;

	private List<Client> clients;

	private List<Phase> phases;

	private List<Site> sites;

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the clients
	 */
	public List<Client> getClients() {
		return clients;
	}

	/**
	 * @param clients
	 *            the clients to set
	 */
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	/**
	 * @return the phases
	 */
	public List<Phase> getPhases() {
		return phases;
	}

	/**
	 * @param phases
	 *            the phases to set
	 */
	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

}
