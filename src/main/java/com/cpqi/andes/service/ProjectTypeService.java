package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.ProjectType;

@Repository
@Transactional
public interface ProjectTypeService extends CrudRepository<ProjectType, Long> {

    ProjectType findById(long id);
    
}
