package com.cpqi.andes.viewmodel;

import java.math.BigDecimal;

/**
 * <p>
 * ReportAllocationViewModel
 * </p>
 *
 * @author Yury Jefse <ysilva@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
public class ReportAllocationViewModel {
	private long idAllocation;
	private String allocationName;
	private String profileName;
	private String phaseName;
	private String clientName;
	private BigDecimal totalHours;
	private BigDecimal totalHoursOffset;
	private BigDecimal price;

	public ReportAllocationViewModel() {
		totalHours = new BigDecimal(0);
		totalHoursOffset = new BigDecimal(0);
		price = new BigDecimal(0);
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public long getIdAllocation() {
		return idAllocation;
	}

	public void setIdAllocation(long idAllocation) {
		this.idAllocation = idAllocation;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getAllocationName() {
		return allocationName;
	}

	public void setAllocationName(String allocationName) {
		this.allocationName = allocationName;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public BigDecimal getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(BigDecimal totalHours) {
		this.totalHours = totalHours;
	}

	public BigDecimal getTotalHoursOffset() {
		return totalHoursOffset;
	}

	public void setTotalHoursOffset(BigDecimal totalHoursOffset) {
		this.totalHoursOffset = totalHoursOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportAllocationViewModel [idAllocation=" + idAllocation + ", allocationName=" + allocationName
				+ ", profileName=" + profileName + ", phaseName=" + phaseName + ", clientName=" + clientName
				+ ", totalHours=" + totalHours + ", totalHoursOffset=" + totalHoursOffset + "]";
	}

}
