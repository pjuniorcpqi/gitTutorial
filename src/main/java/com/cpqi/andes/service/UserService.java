package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.User;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author Yury Silva <ysilva@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface UserService extends CrudRepository<User, Long> {
    
    User findById(long id);

    User findByName(String name);

    User findByEmail(String email);

    User findByActivationToken(String activationToken);
}
