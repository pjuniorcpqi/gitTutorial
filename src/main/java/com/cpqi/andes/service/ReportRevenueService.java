package com.cpqi.andes.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.WorkLog;

/**
 * <p>
 * ReportRevenueService
 * </p>
 *
 * @author Yury Jefse <ysilva@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface ReportRevenueService extends CrudRepository<WorkLog, Long> {
	
	@Query( "SELECT w FROM WorkLog w "
			+ " INNER JOIN w.allocation a "
			+ " INNER JOIN a.user u "
			+ " INNER JOIN a.phase ph "
			+ " INNER JOIN ph.project pr "
			+ " INNER JOIN pr.client c "
			+ " WHERE "
			+ " (w.date >= :startDate AND w.date <= :endDate) "
			+ " AND (pr.id IN (:projectId) OR 0 IN (:projectId)) "
			+ " AND (u.id IN (:userId) OR 0 IN (:userId)) "
			+ " AND (c.id IN (:clientId) OR 0 IN (:clientId)) "
			+ " AND (ph.id IN (:phaseId) OR 0 IN (:phaseId)) "
			+ " ORDER BY "
			+ " pr.client.name, u.name, a.profile.description, pr.title ")
	ArrayList<WorkLog> getRevenue (@Param("userId") List<Long> userId, @Param("startDate") Calendar startDate, @Param("endDate") Calendar endDate, 
		@Param("projectId") List<Long> projectId, @Param("clientId") List<Long> clientId, @Param("phaseId") List<Long> phaseId);
}

