package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.IncomeType;

@Repository
@Transactional
public interface IncomeTypeService extends CrudRepository<IncomeType, Long> {

    IncomeType findById(long id);
    
}
