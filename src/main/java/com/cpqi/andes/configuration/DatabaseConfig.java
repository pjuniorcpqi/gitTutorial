package com.cpqi.andes.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cpqi.andes.configuration.security.AndesCryptoSecurity;

/**
 * <p>
 * DatabaseConfig
 * </p>
 *
 * @author Joel Rocha <jrocha@cpqi.com>
 * @since 0.1
 * @version 0.1
 */

@Configuration
@EnableJpaRepositories(basePackages = { "com.cpqi.andes.service" })
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.cpqi.andes.service", "com.cpqi.andes.service.controller" })
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
    
    private static final String	ADS_DB_URL   = "ADS_DB_URL";
    private static final String	ADS_DB_PW    = "ADS_DB_PW";
    private static final String	ADS_DB_K     = "ADS_DB_K";
    private static final String	ADS_DB_UNAME = "ADS_DB_UNAME";

    @Autowired
    private Environment		env;

    /**
     * DataSource definition for database connection. Settings are
     * read from the
     * application.properties file (using the environment object).
     *
     * @return a valid DataSource.
     */
    @Bean
    public DataSource dataSource() {
	final DriverManagerDataSource dataSource = new DriverManagerDataSource();

	final String dbUrl = System.getenv(ADS_DB_URL);
	final String dbPw = System.getenv(ADS_DB_PW);
	final String key = System.getenv(ADS_DB_K);

	try {
	    dataSource.setDriverClassName(env.getProperty("db.driver"));
	    dataSource.setUsername(System.getenv(ADS_DB_UNAME));
	    
	    final AndesCryptoSecurity crypto = new AndesCryptoSecurity(key);

	    byte[] encryptedValue = crypto.splitPipeArray(dbPw);
	    dataSource.setPassword(new String(crypto.decrypt(encryptedValue), "UTF-8"));

	    encryptedValue = crypto.splitPipeArray(dbUrl);
	    dataSource.setUrl(new String(crypto.decrypt(encryptedValue), "UTF-8"));

	}
	catch (final Exception e) {
	    System.out.println("As variáveis de ambiente com prefixo ADS_DB_* devem estar configuradas no ambiente do servidor.");
	}

	return dataSource;
    }

    /**
     * Declare the JPA entity manager factory.
     *
     * @return A valid LocalContainerEntityManagerFactoryBean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	final LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
	entityManagerFactory.setDataSource(dataSource());
	entityManagerFactory.setPackagesToScan(env.getProperty("entitymanager.packagesToScan"));
	final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
	final Properties additionalProperties = new Properties();
	additionalProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
	additionalProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
	additionalProperties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
	entityManagerFactory.setJpaProperties(additionalProperties);
	return entityManagerFactory;
    }

    /**
     * Declare the transaction manager.
     *
     * @return a transaction manager.
     */
    @Bean
    public JpaTransactionManager transactionManager() {
	final JpaTransactionManager transactionManager = new JpaTransactionManager();
	transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	return transactionManager;
    }

    /**
     * PersistenceExceptionTranslationPostProcessor is a bean post
     * processor
     * which adds an advisor to any bean annotated with Repository so
     * that any
     * platform-specific exceptions are caught and then rethrown as
     * one Spring's
     * unchecked data access exceptions (i.e. a subclass of
     * DataAccessException).
     *
     * @return A valid processor.
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
	return new PersistenceExceptionTranslationPostProcessor();
    }
}
