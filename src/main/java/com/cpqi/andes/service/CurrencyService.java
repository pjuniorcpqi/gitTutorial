package com.cpqi.andes.service;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cpqi.andes.model.Currency;

/**
 * <p>
 * CurrencyService
 * </p>
 * 
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */
@Repository
@Transactional
public interface CurrencyService extends CrudRepository<Currency, Long> {
    
    /**
     * @param code
     * @return a currency
     */
    public Currency findByCode(String code);
    
    public Currency findById(long id);
    
}
