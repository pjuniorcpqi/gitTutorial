package com.cpqi.andes.service;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Holiday;

@Repository
@Transactional
public interface HolidayService extends CrudRepository<Holiday, Long> {

    Holiday findById(long id);

    @Query(value = "SELECT * FROM holidays WHERE holiday_date >= ?1 AND holiday_date <= LAST_DAY (?1)", nativeQuery = true)
    List<Holiday> findHolidayByMoth(Calendar month);

    @Query(value = "SELECT * FROM HOLIDAYS h " 
	    + "INNER JOIN HOLIDAY_SITES hs on h.ID_HOLIDAY = hs.ID_HOLIDAY "
	    + "INNER JOIN SITES s on s.ID_SITE = hs.ID_SITE " 
	    + "WHERE h.HOLIDAY_DATE >= ?1 " 
	    + "AND h.HOLIDAY_DATE <= LAST_DAY (?1) " 
	    + "AND s.ID_SITE = ?2",
	    nativeQuery = true)
    List<Holiday> findHolidayByMonthAndSite(Calendar month, long idSite);
}
