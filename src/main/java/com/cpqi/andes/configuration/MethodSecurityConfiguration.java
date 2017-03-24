package com.cpqi.andes.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@ComponentScan(basePackageClasses = { com.cpqi.andes.controller.UserController.class,
		com.cpqi.andes.controller.TimeSheetController.class })
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(MethodSecurityConfiguration.class);

	public MethodSecurityConfiguration() {
		super();
		logger.info("loading SecurityConfig ................................................ ");
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler handler = new CustomMethodSecurityExpressionHandler();
		return handler;
	}
}
