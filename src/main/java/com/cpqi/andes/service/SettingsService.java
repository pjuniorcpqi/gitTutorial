package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Settings;

/**
 * <p>
 * SettingsService
 * </p>
 * 
 * @author Tiago Morano <tmorano@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface SettingsService extends CrudRepository<Settings, Long>{
	
 	public Settings findById(long id);

}
