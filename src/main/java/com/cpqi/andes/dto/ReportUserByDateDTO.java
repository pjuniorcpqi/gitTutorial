package com.cpqi.andes.dto;

import com.cpqi.andes.model.TimeLog;
import com.cpqi.andes.model.WorkLog;

/**
 * <p>
 * ReportUserByDateDTO
 * </p>
 *
 * @author Yury Jefse <ysilva@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
public class ReportUserByDateDTO {
	private TimeLog timeLog;
	private WorkLog workLog;

	public TimeLog getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(TimeLog timeLog) {
		this.timeLog = timeLog;
	}

	public WorkLog getWorkLog() {
		return workLog;
	}

	public void setWorkLog(WorkLog workLog) {
		this.workLog = workLog;
	}

	public ReportUserByDateDTO(TimeLog t, WorkLog w) {
		this.timeLog = t;
		this.workLog = w;
	}
}
