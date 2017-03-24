package com.cpqi.andes.viewmodel;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * <p>
 * UserLogViewModel
 * </p>
 * .
 *
 * @author Henrique Bezerra <hbezerra@cpqi.com>
 * @version 0.1
 * @since 0.1
 */
public class ReportRevenueViewModel {
    
    /** The id project. */
    private long			idProject;

    /** The project name. */
    private String			projectName;

    /** The client name. */
    private String			clientName;

    /** The project currency. */
    private String			projectCurrency;

    /** The total hours. */
    private BigDecimal			totalHours;

    /** The total amount. */
    private BigDecimal			totalAmount;

    /** The allocations. */
    private ArrayList<UserLogViewModel>	allocations;

    /**
     * Instantiates a new report revenue view model.
     */
    public ReportRevenueViewModel() {
	allocations = new ArrayList<UserLogViewModel>();
	totalHours = new BigDecimal(0);
	totalAmount = new BigDecimal(0);
    }

    /**
     * Gets the total amount.
     *
     * @return the total amount
     */
    public BigDecimal getTotalAmount() {
	return totalAmount;
    }

    /**
     * Sets the total amount.
     *
     * @param totalAmount
     *            the new total amount
     */
    public void setTotalAmount(BigDecimal totalAmount) {
	this.totalAmount = totalAmount;
    }

    /**
     * Gets the id project.
     *
     * @return the id project
     */
    public long getIdProject() {
	return idProject;
    }

    /**
     * Sets the id project.
     *
     * @param idProject
     *            the new id project
     */
    public void setIdProject(long idProject) {
	this.idProject = idProject;
    }

    /**
     * Gets the project name.
     *
     * @return the project name
     */
    public String getProjectName() {
	return projectName;
    }

    /**
     * Sets the project name.
     *
     * @param projectName
     *            the new project name
     */
    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    /**
     * Gets the total hours.
     *
     * @return the total hours
     */
    public BigDecimal getTotalHours() {
	return totalHours;
    }

    /**
     * Sets the total hours.
     *
     * @param totalHours
     *            the new total hours
     */
    public void setTotalHours(BigDecimal totalHours) {
	this.totalHours = totalHours;
    }

    /**
     * Gets the allocations.
     *
     * @return the allocations
     */
    public ArrayList<UserLogViewModel> getAllocations() {
	return allocations;
    }

    /**
     * Sets the allocations.
     *
     * @param allocations
     *            the new allocations
     */
    public void setAllocations(ArrayList<UserLogViewModel> allocations) {
	this.allocations = allocations;
    }

    /**
     * Gets the client name.
     *
     * @return the clientName
     */
    public String getClientName() {
	return clientName;
    }

    /**
     * Sets the client name.
     *
     * @param clientName
     *            the clientName to set
     */
    public void setClientName(String clientName) {
	this.clientName = clientName;
    }

    /**
     * Gets the project currency.
     *
     * @return the project currency
     */
    public String getProjectCurrency() {
	return projectCurrency;
    }

    /**
     * Sets the project currency.
     *
     * @param projectCurrency
     *            the new project currency
     */
    public void setProjectCurrency(String projectCurrency) {
	this.projectCurrency = projectCurrency;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ReportRevenueViewModel [idProject=" + idProject + ", projectName=" + projectName + ", clientName=" + clientName + ", totalHours=" + totalHours
	        + ", totalAmount=" + totalAmount + ", allocations=" + allocations + "]";
    }

}
