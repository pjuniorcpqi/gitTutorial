package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Client;

@Repository
@Transactional
public interface ClientService extends CrudRepository<Client, Long> {
    
    Client findById(long id);

}
