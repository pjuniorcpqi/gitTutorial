package com.cpqi.andes.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 *
 * @author tfacundo
 *
 */

@Entity
@Table(name = "holidays", uniqueConstraints = @UniqueConstraint(columnNames = "holiday_date"))
@SuppressWarnings("serial")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Holiday implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id_holiday")
	private long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "holiday_date", nullable = false)
	@NotNull
	private Calendar holidayDate;

	@Column(nullable = false)
	@NotNull
	private String description;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "holiday_sites", joinColumns = {
			@JoinColumn(name = "id_holiday", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "id_site", nullable = false) })
	private Set<Site> sites = new HashSet<Site>(0);

	public Holiday() {
	}

	public Holiday(Calendar holidayDate, String description) {
		this.holidayDate = holidayDate;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Calendar holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the sites
	 */
	public Set<Site> getSites() {
		return sites;
	}

	/**
	 * @param sites
	 *            the sites to set
	 */
	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Holiday [id=" + id + ", holidayDate=" + (holidayDate == null ? holidayDate : holidayDate.getTime())
				+ ", description=" + description + ", \nsites=" + sites + "]";
	}

}
