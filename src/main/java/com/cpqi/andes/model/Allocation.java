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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.cpqi.andes.configuration.CalendarToDateStringSerializer;
import com.cpqi.andes.configuration.DateStringToCalendarDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * <p>
 * Allocation
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Entity
@Table(name = "allocations")
public class Allocation {

	@Id
	@GeneratedValue
	@Column(name = "id_allocation")
	private long id;

	@ManyToOne
	@JoinColumn(name = "id_phase")
	private Phase phase;

	@Column(name = "price_per_hour")
	private BigDecimal price;

	@Column(name = "hours_per_day")
	private int hoursPerDay;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(nullable = false)
	@NotNull
	private boolean billable;

	@ManyToOne
	@JoinColumn(name = "id_profile")
	private Profile profile;

	@ManyToOne
	@JoinColumn(name = "id_user")
	private User user;

	@Column(name = "start_date", nullable = false)
	@JsonSerialize(using = CalendarToDateStringSerializer.class)
	@JsonDeserialize(using = DateStringToCalendarDeserializer.class)
	private Calendar startDate;

	@Column(name = "end_date", nullable = true)
	@JsonSerialize(using = CalendarToDateStringSerializer.class)
	@JsonDeserialize(using = DateStringToCalendarDeserializer.class)
	private Calendar endDate;

	@Column(name = "total_hours")
	private Integer totalHours;

	@Column(nullable = false)
	private String description;

	/**
	 * Default Constructor
	 */
	public Allocation() {

	}

	/**
	 *
	 *
	 * @param phase
	 *            A project's phase.
	 * @param billable
	 *            If this allocation is billable or not.
	 * @param profile
	 *            The allocation profile.
	 * @param user
	 *            The allocation user.
	 */
	public Allocation(Phase phase, boolean billable, Profile profile, User user) {
		super();
		this.phase = phase;
		this.billable = billable;
		this.profile = profile;
		this.user = user;
	}

	/**
	 * Constructor to billables allocations, creates an allocation with billable
	 * = true.
	 *
	 * @param phase
	 *            A project's phase
	 * @param profile
	 *            The allocation profile.
	 * @param user
	 *            The allocation user.
	 */
	public Allocation(Phase phase, Profile profile, User user) {
		this(phase, true, profile, user);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Allocation [id=" + id + ", profile=" + profile + ", user=" + user + ", phase=" + phase + ", startDate="
				+ (startDate == null ? startDate : startDate.getTime()) + ", endDate="
				+ (endDate == null ? startDate : startDate.getTime()) + ", description=" + description + "]";
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
	 * Getter method to phase.
	 *
	 * @return the phase as Phase
	 */
	public Phase getPhase() {
		return phase;
	}

	/**
	 * Setter name to phase.
	 *
	 * @param phase
	 *            the phase to set
	 */
	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	/**
	 * Getter method to price.
	 *
	 * @return the price as BigDecimal
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Setter name to price.
	 *
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Getter method to hoursPerDay.
	 *
	 * @return the hoursPerDay as int
	 */
	public int getHoursPerDay() {
		return hoursPerDay;
	}

	/**
	 * Setter name to hoursPerDay.
	 *
	 * @param hoursPerDay
	 *            the hoursPerDay to set
	 */
	public void setHoursPerDay(int hoursPerDay) {
		this.hoursPerDay = hoursPerDay;
	}

	/**
	 * Getter method to billable.
	 *
	 * @return the billable as boolean
	 */
	public boolean isBillable() {
		return billable;
	}

	/**
	 * Setter name to billable.
	 *
	 * @param billable
	 *            the billable to set
	 */
	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	/**
	 * Getter method to profile.
	 *
	 * @return the profile as Profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * Setter name to profile.
	 *
	 * @param profile
	 *            the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/**
	 * Getter method to user.
	 *
	 * @return the user as User
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter name to user.
	 *
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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
	 * Getter method to totalHours.
	 *
	 * @return the totalHours as int
	 */
	public Integer getTotalHours() {
		return totalHours;
	}

	/**
	 * Setter name to totalHours.
	 *
	 * @param totalHours
	 *            the totalHours to set
	 */
	public void setTotalHours(Integer totalHours) {
		this.totalHours = totalHours;
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

}