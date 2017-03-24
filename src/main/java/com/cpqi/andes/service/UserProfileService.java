package com.cpqi.andes.service;

import org.springframework.data.repository.CrudRepository;

import com.cpqi.andes.model.UserProfile;

public interface UserProfileService extends CrudRepository<UserProfile, Long> {
    
    UserProfile findById(long id);

}
