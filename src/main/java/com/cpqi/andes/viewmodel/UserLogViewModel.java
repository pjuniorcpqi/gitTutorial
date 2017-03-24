package com.cpqi.andes.viewmodel;

import java.math.BigDecimal;

/**
 * <p>
 * UserLogViewModel
 * </p>
 *
 * @author Yury Jefse <ysilva@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
public class UserLogViewModel {
	private String userName;
	private long idAllocation;
	private String profileName;
	private BigDecimal totalHours;
	private BigDecimal totalHoursOffset;
	private BigDecimal price;
	private String phaseDescription;

	public UserLogViewModel() {
		totalHours = new BigDecimal(0);
		totalHoursOffset = new BigDecimal(0);
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	/**
	 * @return the phaseDescription
	 */
	public String getPhaseDescription() {
		return phaseDescription;
	}

	/**
	 * @param phaseDescription
	 *            the phaseDescription to set
	 */
	public void setPhaseDescription(String phaseDescription) {
		this.phaseDescription = phaseDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserLogViewModel [userName=" + userName + ", idAllocation=" + idAllocation + ", profileName="
				+ profileName + ", totalHours=" + totalHours + ", totalHoursOffset=" + totalHoursOffset
				+ ", phaseDescription=" + phaseDescription + "]";
	}

}
