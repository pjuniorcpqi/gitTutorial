package com.cpqi.andes.viewmodel;

import java.util.Calendar;
import java.util.List;

import com.cpqi.andes.model.Allocation;
import com.cpqi.andes.model.Holiday;
import com.cpqi.andes.model.TimeLog;
import com.cpqi.andes.model.WorkLog;

// TODO: Auto-generated Javadoc
/**
 * The Class TimeSheetViewModel.
 */
public class TimeSheetViewModel {
    
    /** The date. */
    private Calendar	     date;
    
    /** The work logs. */
    private List<WorkLog>    workLogs;
    
    /** The allocations. */
    private List<Allocation> allocations;
    
    /** The time log. */
    private TimeLog	     timeLog;
    
    /** The user name. */
    private String	     userName;
    
    /** The user requesting. */
    private String	     userRequesting;
    
    /** The user id. */
    private long	     userId;
    
    /** The holiday. */
    private Holiday	     holiday;
    
    /** The blocked. */
    private boolean	     blocked;
    
    /** The absence reason id. */
    private long	     absenceReasonId;
    
    /**
     * Instantiates a new time sheet view model.
     */
    public TimeSheetViewModel() {
	
    }
    
    /**
     * Instantiates a new time sheet view model.
     *
     * @param date
     *            the date
     * @param workLogs
     *            the work logs
     * @param allocations
     *            the allocations
     * @param timeLog
     *            the time log
     */
    public TimeSheetViewModel(Calendar date, List<WorkLog> workLogs, List<Allocation> allocations, TimeLog timeLog) {
	super();
	this.date = date;
	this.workLogs = workLogs;
	this.allocations = allocations;
	this.timeLog = timeLog;
    }
    
    /**
     * Getter method to workLogs.
     *
     * @return the workLogs as List<WorkLog>
     */
    public List<WorkLog> getWorkLogs() {
	return workLogs;
    }
    
    /**
     * Setter name to workLogs.
     *
     * @param workLogs
     *            the workLogs to set
     */
    public void setWorkLogs(List<WorkLog> workLogs) {
	this.workLogs = workLogs;
    }
    
    /**
     * Getter method to timeLog.
     *
     * @return the timeLog as TimeLog
     */
    public TimeLog getTimeLog() {
	return timeLog;
    }
    
    /**
     * Setter name to timeLog.
     *
     * @param timeLog
     *            the timeLog to set
     */
    public void setTimeLog(TimeLog timeLog) {
	this.timeLog = timeLog;
    }
    
    /**
     * Getter method to allocations.
     *
     * @return the allocations as List<Allocation>
     */
    public List<Allocation> getAllocations() {
	return allocations;
    }
    
    /**
     * Setter name to allocations.
     *
     * @param allocations
     *            the allocations to set
     */
    public void setAllocations(List<Allocation> allocations) {
	this.allocations = allocations;
    }
    
    /**
     * Getter method to date.
     *
     * @return the date as Date
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
    public void Calendar(Calendar date) {
	this.date = date;
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
     * Getter method to userName.
     *
     * @return the userName as String
     */
    public String getUserName() {
	return userName;
    }
    
    /**
     * Setter name to userName.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
	this.userName = userName;
    }
    
    /**
     * Getter method to holiday.
     *
     * @return the holiday as Holiday
     */
    public Holiday getHoliday() {
	return holiday;
    }
    
    /**
     * Setter name to holiday.
     *
     * @param holiday
     *            the holiday to set
     */
    public void setHoliday(Holiday holiday) {
	this.holiday = holiday;
    }
    
    /**
     * Checks if is blocked.
     *
     * @return the blocked
     */
    public boolean isBlocked() {
	return blocked;
    }
    
    /**
     * Sets the blocked.
     *
     * @param blocked
     *            the blocked to set
     */
    public void setBlocked(boolean blocked) {
	this.blocked = blocked;
    }
    
    /**
     * Gets the user requesting.
     *
     * @return the user requesting
     */
    public String getUserRequesting() {
	return userRequesting;
    }
    
    /**
     * Sets the user requesting.
     *
     * @param userRequesting
     *            the new user requesting
     */
    public void setUserRequesting(String userRequesting) {
	this.userRequesting = userRequesting;
    }
    
    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public long getUserId() {
	return userId;
    }
    
    /**
     * Sets the user id.
     *
     * @param userId
     *            the new user id
     */
    public void setUserId(long userId) {
	this.userId = userId;
    }
    
    /**
     * Gets the absence reason id.
     *
     * @return the absence reason id
     */
    public long getAbsenceReasonId() {
	return absenceReasonId;
    }
    
    /**
     * Sets the absence reason id.
     *
     * @param absenceReasonId
     *            the new absence reason id
     */
    public void setAbsenceReasonId(long absenceReasonId) {
	this.absenceReasonId = absenceReasonId;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TimeSheetViewModel [date=" + (date == null ? date : date.getTime()) + ", workLogs=" + workLogs + ", allocations=" + allocations + ", timeLog="
	        + timeLog + ", userName=" + userName + ", userRequesting=" + userRequesting + ", userId=" + userId + ", holiday=" + holiday + ", blocked="
	        + blocked + ", absenceReasonId=" + absenceReasonId + "]";
    }
    
}
