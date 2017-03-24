package com.cpqi.andes.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.TimeLog;

/**
 * <p>
 * TimeLogService
 * </p>
 *
 * @author Pedro Marcos <pjunior@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface TimeLogService extends CrudRepository<TimeLog, Long> {
    
    public TimeLog findById(long id);
    
    @Query(value = "SELECT * from timelogs t where t.date_of_work = ?1 and t.id_user = ?2", nativeQuery = true)
    TimeLog findByDateAndUser(Calendar date, Long idUser);
    
    @Query(value = "select * from timelogs t where to_number(to_char(t.date_of_work, 'mm')) = (?1) and t.id_user = (?2)", nativeQuery = true)
    List<TimeLog> findAllByMonthAndUser(int month, Long idUser);
    
    @Query(value = "Select U.Id_User, U.Name, T.Date_Of_Work, T.In_Time_1, T.Out_Time_1, T.In_Time_2, T.Out_Time_2, T.In_Time_3, T.Out_Time_3, "
	    + "pr.title, c.name as nameC, ph.description, pf.description as dscptPf, al.id_allocation, w.Date_of_work as dateW, w.time_inserted "
	    + "From Timelogs T "
	    + "Inner Join Users U On U.Id_User = T.Id_User "
	    + "Left Outer Join Allocations Al On Al.Id_User = U.Id_User "
	    + "Left Outer Join Worklogs W On W.Id_Allocation = Al.Id_Allocation "
	    + "Inner Join Phases Ph On Ph.Id_Phase = Al.Id_Phase "
	    + "Inner Join Projects Pr On Pr.Id_Project = Ph.Id_Project "
	    + "Inner Join Profiles Pf On Pf.Id_Profile = Al.Id_Profile "
	    + "Inner Join Clients C On C.Id_Client = Pr.Id_Client "
	    + "Where (U.Id_User In (:userId) Or 0 In (:userId)) "
	    + "And (Pr.Id_Project In (:projectId) Or 0 In (:projectId)) "
	    + "And (W.Id_Worklog Is Null Or T.Date_Of_Work = W.Date_Of_Work) "
	    + "And (T.Date_Of_Work >= :startDate and T.Date_Of_Work <= :endDate ) "
	    + "And (Al.Id_User In (:userId) or 0 In (:userId)) "
	    + "And (Pr.Id_Client In (:clientId) Or 0 In (:clientId)) "
	    + "And (Ph.Id_Phase In (:phaseId) Or 0 In (:phaseId)) "
	    + "Order By T.Id_User, T.Date_Of_Work", nativeQuery = true)
    ArrayList<Object> filterReportUserHours(@Param("userId") List<Long> userId, @Param("startDate") Calendar startDate, @Param("endDate") Calendar endDate,
            @Param("projectId") List<Long> projectId, @Param("clientId") List<Long> clientId, @Param("phaseId") List<Long> phaseId); 
}
