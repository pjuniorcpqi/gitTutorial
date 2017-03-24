package com.cpqi.andes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>
 * Currency
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Entity
@Table(name = "currencies")
public class Currency {

	@Id
	@GeneratedValue
	@Column(name = "id_currency")
	private long id;

	private String currency;

	private String code;

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
	 * Getter method to currency.
	 *
	 * @return the currency as String
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Setter name to currency.
	 *
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * Getter method to code.
	 *
	 * @return the code as String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Setter name to code.
	 *
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Currency [id=" + id + ", currency=" + currency + ", code=" + code + "]";
	}

}
