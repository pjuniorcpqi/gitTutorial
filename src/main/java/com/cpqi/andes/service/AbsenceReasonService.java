package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.AbsenceReason;

@Repository
@Transactional
public interface AbsenceReasonService extends CrudRepository<AbsenceReason, Long> {
    
    AbsenceReason findById(long id);
    
}
