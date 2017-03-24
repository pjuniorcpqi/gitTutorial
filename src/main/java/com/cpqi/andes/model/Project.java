package com.cpqi.andes.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * <p>
 * Project
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Entity
@Table(name = "projects")
public class Project {

	@Id
	@GeneratedValue
	@Column(name = "id_project")
	private long id;

	@Column(nullable = false)
	private String title;

	@ManyToOne
	@JoinColumn(name = "id_client")
	private Client client;

	@OneToOne
	@JoinColumn(name = "id_project_type")
	private ProjectType projectType;

	@OneToOne
	@JoinColumn(name = "id_income_type")
	private IncomeType incomeType;

	@ManyToOne
	@JoinColumn(name = "id_currency")
	private Currency currency;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date")
	private Calendar startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date")
	private Calendar endDate;

	private String description;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean internal;

	/**
	 * Default constructor
	 */
	public Project() {
	}

	/**
	 *
	 *
	 * @param title
	 *            Project's title
	 * @param internal
	 *            True when is an internal project, false otherwise.
	 * @param phases
	 *            All phases to this project, a project must have at least one
	 *            phase.
	 *
	 */
	public Project(String title, boolean internal) {
		this.title = title;
		this.internal = internal;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Project [id=" + id + ", title=" + title + ", client=" + client + ", projectType=" + projectType
				+ ", currency=" + currency + ", startDate=" + (startDate == null ? startDate : startDate.getTime())
				+ ", endDate=" + (endDate == null ? endDate : endDate.getTime()) + ", description=" + description
				+ ", internal=" + internal + "]";
	}

	/**
	 * Getter method to id.
	 *
	 * @return the id as long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter name to id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter method to title.
	 *
	 * @return the title as String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter name to title.
	 *
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter method to description.
	 *
	 * @return the description as String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter name to description.
	 *
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter method to internal.
	 *
	 * @return the internal as boolean
	 */
	public boolean isInternal() {
		return internal;
	}

	/**
	 * Setter name to internal.
	 *
	 * @param internal
	 *            the internal to set
	 */
	public void setInternal(boolean internal) {
		this.internal = internal;
	}

	/**
	 * Getter method to projectType.
	 *
	 * @return the projectType as ProjectTypes
	 */
	public ProjectType getProjectType() {
		return projectType;
	}

	/**
	 * Setter name to projectType.
	 *
	 * @param projectType
	 *            the projectType to set
	 */
	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	/**
	 * Getter method to startDate.
	 *
	 * @return the startDate as Calendar
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * Setter name to startDate.
	 *
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter method to endDate.
	 *
	 * @return the endDate as Calendar
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Setter name to endDate.
	 *
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter method to incomeType.
	 *
	 * @return the incomeType as IncomeTypes
	 */
	public IncomeType getIncomeType() {
		return incomeType;
	}

	/**
	 * Setter name to incomeType.
	 *
	 * @param incomeType
	 *            the incomeType to set
	 */
	public void setIncomeType(IncomeType incomeType) {
		this.incomeType = incomeType;
	}

	/**
	 * Getter method to client.
	 *
	 * @return the client as Clients
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Setter name to client.
	 *
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * Getter method to currency.
	 *
	 * @return the currency as Currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Setter name to currency.
	 *
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
