package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Site;

/**
 * <p>
 * SiteService
 * </p>
 * 
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface SiteService extends CrudRepository<Site, Long>{
    public Site findById(long id);
    public Site findByName(String name);
}
