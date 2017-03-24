package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Phase;
import com.cpqi.andes.model.Profile;

/**
 * <p>
 * PhaseService class
 * </p>
 *
 * @author Moises Albuquerque <malbuquerque@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface ProfileService extends CrudRepository<Profile, Long> {
    
	Phase findById(long id);

}
