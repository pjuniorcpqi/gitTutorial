package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Project;

@Repository
@Transactional
public interface ProjectService extends CrudRepository<Project, Long> {
    
    Project findById(long id);

    Project findByTitle(String title);
    
}
