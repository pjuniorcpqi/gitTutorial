package com.cpqi.andes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "settings")
public class Settings {

	@Id
	@GeneratedValue
	@Column(name = "id_settings")
	private long id;

	@Column(name = "block_timesheet")
	private int blockTimeSheet;

	@Column(name = "view_mode_timesheet")
	private String viewModeTimesheet;

	/**
	 * @return the viewModeTimesheet
	 */
	public String getViewModeTimesheet() {
		return viewModeTimesheet;
	}

	/**
	 * @param viewModeTimesheet
	 *            the viewModeTimesheet to set
	 */
	public void setViewModeTimesheet(String viewModeTimesheet) {
		this.viewModeTimesheet = viewModeTimesheet;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the blockTimeSheet
	 */
	public int getBlockTimeSheet() {
		return blockTimeSheet;
	}

	/**
	 * @param blockTimeSheet
	 *            the blockTimeSheet to set
	 */
	public void setBlockTimeSheet(int blockTimeSheet) {
		this.blockTimeSheet = blockTimeSheet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Settings [id=" + id + ", blockTimeSheet=" + blockTimeSheet + ", viewModeTimesheet=" + viewModeTimesheet
				+ "]";
	}

}
