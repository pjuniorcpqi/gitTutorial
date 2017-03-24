package com.cpqi.andes.viewmodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * ReportUserByDateViewModel
 * </p>
 *
 * @author Yury Jefse <ysilva@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
public class ReportUserByDateViewModel {

	public ReportUserByDateViewModel() {
		this.allocations = new ArrayList<ReportAllocationViewModel>();
		this.totalHours = BigDecimal.ZERO;
	}

	public ReportUserByDateViewModel(BigDecimal totalHours, String userName,
			ArrayList<ReportAllocationViewModel> allocations) {
		this.totalHours = totalHours;
		this.userName = userName;
		this.allocations = allocations;
	}

	private BigDecimal totalHours;
	private String userName;
	private final ArrayList<ReportAllocationViewModel> allocations;
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public BigDecimal getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(BigDecimal totalHours) {
		this.totalHours = totalHours;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<ReportAllocationViewModel> getAllocations() {
		return allocations;
	}

	public void setAllocations(HashMap<Long, ReportAllocationViewModel> mapAllocations) {
		Iterator<Map.Entry<Long, ReportAllocationViewModel>> iterator = mapAllocations.entrySet().iterator();
		while (iterator.hasNext()) {
			getAllocations().add(iterator.next().getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportUserByDateViewModel [totalHours=" + totalHours + ", userName=" + userName + ", allocations="
				+ allocations + ", userId=" + userId + "]";
	}

}
