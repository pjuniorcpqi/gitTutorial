package com.cpqi.andes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * Profile
 * </p>
 * 
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Entity
@Table(name = "profiles")
public class Profile {

	@Id
	@GeneratedValue
	@Column(name = "id_profile")
	private long id;

	@Column(nullable = false)
	private String description;

	/**
	 * Default constructor
	 */
	public Profile() {
	}

	/**
	 * 
	 * @param description
	 *            A description to this profile.
	 */
	public Profile(String description) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Profile [id=" + id + ", description=" + description + "]";
	}
}
