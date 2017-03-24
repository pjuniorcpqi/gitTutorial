package com.cpqi.andes.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cpqi.andes.configuration.CalendarToDateStringSerializer;
import com.cpqi.andes.configuration.DateStringToCalendarDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * <p>
 * Phase
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Entity
@Table(name = "phases")
public class Phase {

	@Id
	@GeneratedValue
	@Column(name = "id_phase")
	private long id;

	private String description;

	@Column(name = "start_date", nullable = false)
	@Temporal(TemporalType.DATE)
	@JsonSerialize(using = CalendarToDateStringSerializer.class)
	@JsonDeserialize(using = DateStringToCalendarDeserializer.class)
	private Calendar startDate;

	@Column(name = "end_date", nullable = false)
	@Temporal(TemporalType.DATE)
	@JsonSerialize(using = CalendarToDateStringSerializer.class)
	@JsonDeserialize(using = DateStringToCalendarDeserializer.class)
	private Calendar endDate;

	@ManyToOne
	@JoinColumn(name = "id_project")
	private Project project;

	/**
	 * Default constructor
	 */
	public Phase() {

	}

	/**
	 *
	 *
	 * @param description
	 */
	public Phase(String description) {
		this.description = description;
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
	 * Getter method to endtDate.
	 *
	 * @return the endtDate as Calendar
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Setter name to endtDate.
	 *
	 * @param endtDate
	 *            the endtDate to set
	 */
	public void setEndDate(Calendar endtDate) {
		this.endDate = endtDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Phase [id=" + id + ", description=" + description + ", startDate="
				+ (startDate == null ? startDate : startDate.getTime()) + ", endtDate="
				+ (endDate == null ? endDate : endDate.getTime()) + "]";
	}

	/**
	 * Getter method to project.
	 *
	 * @return the project as Project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Setter name to project.
	 *
	 * @param project
	 *            the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

}
