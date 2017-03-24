package com.cpqi.andes.model;

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

// TODO: Auto-generated Javadoc
/**
 * <p>
 * User
 * </p>
 * .
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @version 0.1
 * @since 0.1
 */
@Entity
@Table(name = "users")
public class User {
    
    /** The id. */
    @Id
    @GeneratedValue
    @Column(name = "id_user")
    private long	id;
    
    /** The name. */
    @Column(nullable = false)
    @NotNull
    private String	name;
    
    /** The email. */
    @Column(nullable = false)
    @NotNull
    private String	email;
    
    /** The access level. */
    @ManyToOne
    @JoinColumn(nullable = false, name = "id_access_level")
    private AccessLevel	accessLevel;
    
    /** The site. */
    @ManyToOne
    @JoinColumn(nullable = false, name = "id_site")
    private Site	site;
    
    /** The active. */
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean	active;
    
    /** The password. */
    @Column(nullable = false)
    private String	password;
    
    /** The activation token. */
    @Column(name = "activation_token")
    private String	activationToken;
    
    /** The admission date. */
    @Column(name = "admission_date")
    private Calendar	admissionDate;
    
    /** The entrance time. */
    @Column(name = "entrance_time")
    private String	entranceTime;
    
    /** The exit time. */
    @Column(name = "exit_time")
    private String	exitTime;
    
    /** The working hours. */
    @Column(name = "working_hours")
    private String	workingHours;
    
    /** The pis pasep. */
    @Column(name = "pis")
    private String	pis;
    
    /** The interval. */
    @Column(name = "interval")
    private int		interval;
    
    /**
     * Default constructor.
     */
    public User() {
	
    }
    
    /**
     * Constructor.
     *
     * @param name
     *            User's name.
     * @param email
     *            User's email.
     */
    public User(String name, String email) {
	
	this.name = name;
	this.email = email;
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
    
    /**
     * Getter method to email.
     *
     * @return the email as String
     */
    public String getEmail() {
	return email;
    }
    
    /**
     * Setter name to email.
     *
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
	this.email = email;
    }
    
    /**
     * Getter method to accessLevel.
     *
     * @return the accessLevel as AccessLevel
     */
    public AccessLevel getAccessLevel() {
	return accessLevel;
    }
    
    /**
     * Setter name to accessLevel.
     *
     * @param accessLevel
     *            the accessLevel to set
     */
    public void setAccessLevel(AccessLevel accessLevel) {
	this.accessLevel = accessLevel;
    }
    
    /**
     * Getter method to active.
     *
     * @return the active as boolean
     */
    public boolean isActive() {
	return active;
    }
    
    /**
     * Setter name to active.
     *
     * @param active
     *            the active to set
     */
    public void setActive(boolean active) {
	this.active = active;
    }
    
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
	return password;
    }
    
    /**
     * Sets the password.
     *
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
	this.password = password;
    }
    
    /**
     * Gets the site.
     *
     * @return the site
     */
    public Site getSite() {
	return site;
    }
    
    /**
     * Sets the site.
     *
     * @param site
     *            the new site
     */
    public void setSite(Site site) {
	this.site = site;
    }
    
    /**
     * Gets the activation token.
     *
     * @return the activation token
     */
    public String getActivationToken() {
	return activationToken;
    }

    /**
     * Sets the activation token.
     *
     * @param activationToken
     *            the new activation token
     */
    public void setActivationToken(String activationToken) {
	this.activationToken = activationToken;
    }

    /**
     * Gets the admission date.
     *
     * @return the admission date
     */
    public Calendar getAdmissionDate() {
	return admissionDate;
    }
    
    /**
     * Sets the admission date.
     *
     * @param admissionDate
     *            the new admission date
     */
    public void setAdmissionDate(Calendar admissionDate) {
	this.admissionDate = admissionDate;
    }
    
    /**
     * Gets the entrance time.
     *
     * @return the entrance time
     */
    public String getEntranceTime() {
	return entranceTime;
    }
    
    /**
     * Sets the entrance time.
     *
     * @param entranceTime
     *            the new entrance time
     */
    public void setEntranceTime(String entranceTime) {
	this.entranceTime = entranceTime;
    }
    
    /**
     * Gets the exit time.
     *
     * @return the exit time
     */
    public String getExitTime() {
	return exitTime;
    }
    
    /**
     * Sets the exit time.
     *
     * @param exitTime
     *            the new exit time
     */
    public void setExitTime(String exitTime) {
	this.exitTime = exitTime;
    }
    
    /**
     * Gets the working hours.
     *
     * @return the working hours
     */
    public String getWorkingHours() {
	return workingHours;
    }
    
    /**
     * Sets the working hours.
     *
     * @param workingHours
     *            the new working hours
     */
    public void setWorkingHours(String workingHours) {
	this.workingHours = workingHours;
    }
    
    /**
     * Gets the pis.
     *
     * @return the pis
     */
    public String getPis() {
	return pis;
    }
    
    /**
     * Sets the pis.
     *
     * @param pis
     *            the new pis
     */
    public void setPis(String pis) {
	this.pis = pis;
    }
    
    /**
     * Gets the interval.
     *
     * @return the interval
     */
    public int getInterval() {
	return interval;
    }
    
    /**
     * Sets the interval.
     *
     * @param interval
     *            the new interval
     */
    public void setInterval(int interval) {
	this.interval = interval;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "User [id=" + id + ", name=" + name + ", email=" + email + ", status=" + active + "]";
    }
}
