package com.cpqi.andes.service;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Allocation;
import com.cpqi.andes.model.WorkLog;

/**
 * <p>
 * WorklogService
 * </p>
 *
 * @author Pedro Marcos <pjunior@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface WorkLogService extends CrudRepository<WorkLog, Long> {

	WorkLog findById(long id);

	@Query(value = "SELECT * from worklogs w where w.date_of_work = ?1 and w.id_allocation = ?2", nativeQuery = true)
	WorkLog findByDateAndAllocation(Calendar date, Long idAllocation);

	@Async
	@Query(value = "SELECT * from worklogs w inner join allocations a on w.id_allocation = a.id_allocation and a.id_user=:idUser and w.date_of_work BETWEEN :startDate AND :endDate", nativeQuery = true)
	List<WorkLog> findByUserAndDateRange(@Param("idUser") Long idUser, @Param("startDate") Calendar startDate,
			@Param("endDate") Calendar endDate);

	Set<WorkLog> findByAllocation(Allocation allocation);
}
