/**
 * <p>
 * Site
 * </p>
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
package com.cpqi.andes.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>
 * Site
 * </p>
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @since 0.1
 * @version 0.1
 */

@Entity
@Table(name = "sites")
@SuppressWarnings("serial")
public class Site implements Serializable {

	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "id_site")
	private long id;

	/** The name. */
	private String name;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "sites")
	private Set<Holiday> holidays = new HashSet<Holiday>(0);

	public Site() {
	}

	public Site(String name) {
		this.name = name;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the holidays
	 */
	public Set<Holiday> getHolidays() {
		return holidays;
	}

	/**
	 * @param holidays
	 *            the holidays to set
	 */
	public void setHolidays(Set<Holiday> holidays) {
		this.holidays = holidays;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Site [id=" + id + ", name=" + name + "]";
	}

}
