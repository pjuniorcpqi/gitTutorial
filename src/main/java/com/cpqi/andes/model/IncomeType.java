package com.cpqi.andes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * IncomeTypes
 * </p>
 *
 * @author Tiago Morano <tmorano@cpqi.com>
 * @since 0.1
 * @version 0.1
 *
 */

@Entity
@Table(name = "income_types")
public class IncomeType {

	@Id
	@GeneratedValue
	@Column(name = "id_income_type")
	private long id;

	@Column(nullable = false)
	private String name;

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
	 * Getter method to name.
	 *
	 * @return the name as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter name to name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IncomeType [id=" + id + ", name=" + name + "]";
	}
}
