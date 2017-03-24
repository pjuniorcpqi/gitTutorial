package com.cpqi.andes.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Allocation;

/**
 * <p>
 * AllocationService
 * </p>
 *
 * @author Pedro Marcos <pjunior@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface AllocationService extends CrudRepository<Allocation, Long> {

    /**
     * @param code
     * @return
     */
    Allocation findById(long id);

    /**
     * @param startDate
     * @param endDate
     * @param idUser
     * @return
     */
    @Query(value = "select * from allocations a where " + "a.id_user = ?1 and ?2 >= trunc(a.start_date) "
	    + "and (a.end_date is null or ?2 <= trunc(a.end_date))", nativeQuery = true)
    ArrayList<Allocation> findAllAllocationsByDateAndUser(Long idUser, Calendar date);
    
    @Async
    @Query(value = "SELECT * from allocations a where a.id_user= ?1 and ?2 <= trunc(a.start_date) and (a.end_date is null or ?3 >= trunc(a.end_date))",
    nativeQuery = true)
    ArrayList<Allocation> findAllAllocationsByUserAndMonth(Long idUser, Calendar startDate, Calendar endDate);
    
    /**
     * @param code
     * @return
     */
    ArrayList<Allocation> findByUserId(long userId);
    
    @Query(value = "select * from allocations a where a.id_user is null", nativeQuery = true)
    List<Allocation> findAllAllocationsWithNoUser();
    
}
