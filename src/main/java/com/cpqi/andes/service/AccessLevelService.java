package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.AccessLevel;

/**
 * <p>
 * SiteController
 * </p>
 * .
 *
 * @author Henrique Guedes <hbezerra@cpqi.com>
 * @version 0.1
 * @since 0.1
 */

@Repository
@Transactional
public interface AccessLevelService extends CrudRepository<AccessLevel, Long> {
    
    AccessLevel findById(long id);

    AccessLevel findByDescription(String description);

}
