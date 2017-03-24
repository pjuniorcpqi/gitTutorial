package com.cpqi.andes.model;

import java.util.Calendar;
import java.util.List;

public class ReportFilterUserByDate {

	private Calendar startDate;

	private Calendar endDate;

	private List<Long> userId;

	private List<Long> projectId;

	private List<Long> clientId;

	private List<Long> phaseId;

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public List<Long> getUserId() {
		return userId;
	}

	public void setUserId(List<Long> userId) {
		this.userId = userId;
	}

	public List<Long> getProjectId() {
		return projectId;
	}

	public void setProjectId(List<Long> projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the clientId
	 */
	public List<Long> getClientId() {
		return clientId;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(List<Long> clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the phaseId
	 */
	public List<Long> getPhaseId() {
		return phaseId;
	}

	/**
	 * @param phaseId
	 *            the phaseId to set
	 */
	public void setPhaseId(List<Long> phaseId) {
		this.phaseId = phaseId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportFilterUserByDate [startDate=" + (startDate == null ? startDate : startDate.getTime())
				+ ", endDate=" + (endDate == null ? endDate : endDate.getTime()) + ", userId=" + userId + ", projectId="
				+ projectId + ", clientId=" + clientId + ", phaseId=" + phaseId + "]";
	}

}
