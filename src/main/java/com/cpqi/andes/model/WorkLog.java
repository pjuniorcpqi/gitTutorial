package com.cpqi.andes.model;

import java.math.BigDecimal;
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

import org.hibernate.annotations.Type;

/**
 * <p>
 * WorkLog
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Entity
@Table(name = "worklogs")
public class WorkLog {

	@Id
	@GeneratedValue
	@Column(name = "id_worklog")
	private long id;

	@Column(name = "date_of_work")
	@Temporal(TemporalType.DATE)
	private Calendar date;

	@ManyToOne
	@JoinColumn(name = "id_allocation")
	private Allocation allocation;

	@Column(name = "comments")
	private String comments;

	@Column(name = "time_inserted")
	private BigDecimal timeInserted;

	@Column(name = "time_offset_admin")
	private BigDecimal timeOffsetAdmin;

	@Column(name = "revenue_lock", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean revenueLock;

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
	 * Getter method to date.
	 *
	 * @return the date as Calendar
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Setter name to date.
	 *
	 * @param date
	 *            the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Getter method to allocation.
	 *
	 * @return the allocation as Allocation
	 */
	public Allocation getAllocation() {
		return allocation;
	}

	/**
	 * Setter name to allocation.
	 *
	 * @param allocation
	 *            the allocation to set
	 */
	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	/**
	 * Getter method to comments.
	 *
	 * @return the comments as String
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Setter name to comments.
	 *
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * Getter method to timeInserted.
	 *
	 * @return the timeInserted as BigDecimal
	 */
	public BigDecimal getTimeInserted() {
		return timeInserted;
	}

	/**
	 * Setter name to timeInserted.
	 *
	 * @param timeInserted
	 *            the timeInserted to set
	 */
	public void setTimeInserted(BigDecimal timeInserted) {
		this.timeInserted = timeInserted;
	}

	/**
	 * Getter method to timeOffsetAdmin.
	 *
	 * @return the timeOffsetAdmin as BigDecimal
	 */
	public BigDecimal getTimeOffsetAdmin() {
		return timeOffsetAdmin;
	}

	/**
	 * Setter name to timeOffsetAdmin.
	 *
	 * @param timeOffsetAdmin
	 *            the timeOffsetAdmin to set
	 */
	public void setTimeOffsetAdmin(BigDecimal timeOffsetAdmin) {
		this.timeOffsetAdmin = timeOffsetAdmin;
	}

	public boolean isRevenueLock() {
		return revenueLock;
	}

	public void setRevenueLock(boolean revenueLock) {
		this.revenueLock = revenueLock;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorkLog [id=" + id + ", date="
				+ (date == null ? date : date.getTime()) + ", comments="
				+ comments + ", timeInserted=" + timeInserted
				+ ", timeOffsetAdmin=" + timeOffsetAdmin + "revenueLock="
				+ revenueLock + "]";
	}

}
