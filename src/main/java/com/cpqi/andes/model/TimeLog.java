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
 * TimeLog
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Entity
@Table(name = "timelogs")
public class TimeLog {
    
    @Id
    @GeneratedValue
    @Column(name = "id_timelog")
    private long	  id;
    
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User	  user;
    
    @Column(name = "date_of_work")
    @Temporal(TemporalType.DATE)
    private Calendar	  date;
    
    @Column(name = "in_time_1")
    private String	  inTime1;
    
    @Column(name = "in_time_2")
    private String	  inTime2;
    
    @Column(name = "in_time_3")
    private String	  inTime3;
    
    @Column(name = "out_time_1")
    private String	  outTime1;
    
    @Column(name = "out_time_2")
    private String	  outTime2;
    
    @Column(name = "out_time_3")
    private String	  outTime3;
    
    @Column(name = "inserted_by_admin", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean	  insertedByAdmin;
    
    @OneToOne
    @JoinColumn(name = "id_absence_reason")
    private AbsenceReason absenceReason;
    
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
     * Getter method to inTime1.
     *
     * @return the inTime1 as String
     */
    public String getInTime1() {
	return inTime1;
    }
    
    /**
     * Setter name to inTime1.
     *
     * @param inTime1
     *            the inTime1 to set
     */
    public void setInTime1(String inTime1) {
	this.inTime1 = inTime1;
    }
    
    /**
     * Getter method to inTime2.
     *
     * @return the inTime2 as String
     */
    public String getInTime2() {
	return inTime2;
    }
    
    /**
     * Setter name to inTime2.
     *
     * @param inTime2
     *            the inTime2 to set
     */
    public void setInTime2(String inTime2) {
	this.inTime2 = inTime2;
    }
    
    /**
     * Getter method to inTime3.
     *
     * @return the inTime3 as String
     */
    public String getInTime3() {
	return inTime3;
    }
    
    /**
     * Setter name to inTime3.
     *
     * @param inTime3
     *            the inTime3 to set
     */
    public void setInTime3(String inTime3) {
	this.inTime3 = inTime3;
    }
    
    /**
     * Getter method to outTime1.
     *
     * @return the outTime1 as String
     */
    public String getOutTime1() {
	return outTime1;
    }
    
    /**
     * Setter name to outTime1.
     *
     * @param outTime1
     *            the outTime1 to set
     */
    public void setOutTime1(String outTime1) {
	this.outTime1 = outTime1;
    }
    
    /**
     * Getter method to outTime2.
     *
     * @return the outTime2 as String
     */
    public String getOutTime2() {
	return outTime2;
    }
    
    /**
     * Setter name to outTime2.
     *
     * @param outTime2
     *            the outTime2 to set
     */
    public void setOutTime2(String outTime2) {
	this.outTime2 = outTime2;
    }
    
    /**
     * Getter method to outTime3.
     *
     * @return the outTime3 as String
     */
    public String getOutTime3() {
	return outTime3;
    }
    
    /**
     * Setter name to outTime3.
     *
     * @param outTime3
     *            the outTime3 to set
     */
    public void setOutTime3(String outTime3) {
	this.outTime3 = outTime3;
    }
    
    /**
     * @return the insertedByAdmin
     */
    public boolean isInsertedByAdmin() {
	return insertedByAdmin;
    }
    
    /**
     * @param insertedByAdmin
     *            the insertedByAdmin to set
     */
    public void setInsertedByAdmin(boolean insertedByAdmin) {
	this.insertedByAdmin = insertedByAdmin;
    }
    
    /**
     * Gets the absence reason.
     *
     * @return the absence reason
     */
    public AbsenceReason getAbsenceReason() {
	return absenceReason;
    }

    /**
     * Sets the absence reason.
     *
     * @param absenceReason
     *            the new absence reason
     */
    public void setAbsenceReason(AbsenceReason absenceReason) {
	this.absenceReason = absenceReason;
    }

    /**
     * @param insertedByAdmin
     *            the insertedByAdmin to set
     */
    @Override
    public String toString() {
	return "TimeLog [id=" + id + ", user=" + user + ", date=" + (date == null ? date : date.getTime()) + ", inTime1=" + inTime1 + ", inTime2=" + inTime2
	        + ", inTime3=" + inTime3 + ", outTime1=" + outTime1 + ", outTime2=" + outTime2 + ", outTime3=" + outTime3 + ", absenceReason=" + absenceReason
	        + ", insertedByAdmin=" + insertedByAdmin + "]";
    }
    
}
