package com.cpqi.andes.configuration;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private static final Logger	logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService		userDetailsService;

    public SecurityConfig() {
	super();
	logger.info("loading SecurityConfig ................................................ ");
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
	auth.authenticationProvider(authenticationProvider());

    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
	List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
	accessDecisionVoters.add(webExpressionVoter());

	AffirmativeBased accessDecisionManager = new AffirmativeBased(accessDecisionVoters);
	return accessDecisionManager;
    }

    @Bean
    public WebExpressionVoter webExpressionVoter() {
	WebExpressionVoter webExpressionVoter = new CustomWebExpressionVoter();
	return webExpressionVoter;
    }

    private void configVoter(HttpSecurity http) throws Exception {
	http.authorizeRequests().accessDecisionManager(accessDecisionManager());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	configVoter(http);

	// http.csrf().disable();

	// session management
	http.sessionManagement().maximumSessions(1);

	http.authorizeRequests().antMatchers("/timelogs/**").access(getTimeLogPermissions());
	http.authorizeRequests().antMatchers("/timelogsAdmin/**").access(adminAndManagerAccess());
	http.authorizeRequests().antMatchers("/users/").access(adminAccess());
	http.authorizeRequests().antMatchers("/user/{\\d+}").access(adminAccess());
	http.authorizeRequests().antMatchers("/user/").access(adminAccess());
	http.authorizeRequests().antMatchers("/signup/").permitAll();
	http.authorizeRequests().antMatchers("/setuser/{\\d+}").authenticated();
	http.authorizeRequests().antMatchers("/getuser/").authenticated();
	http.authorizeRequests().antMatchers("/projects/**").access(adminAndManagerAccess());
	http.authorizeRequests().antMatchers("/allocations/**").access(adminAndManagerAccess());
	http.authorizeRequests().antMatchers("/sites/{\\w}").access(adminAccess());
	http.authorizeRequests().antMatchers("/settings/**").access(adminAccess());
	http.authorizeRequests().antMatchers("/unlockTimeLog/").access(adminAccess());
	http.authorizeRequests().antMatchers("/sites/").permitAll();
	http.authorizeRequests().antMatchers("/settingsValues/").permitAll();
	http.authorizeRequests().antMatchers("/holidays/**").access(adminAccess());
	http.authorizeRequests().antMatchers("/clients/**").access(adminAccess());
	http.authorizeRequests().antMatchers("/currencies/**").access(adminAccess());
	http.authorizeRequests().antMatchers("worklogs/**").access(adminAccess());
	http.authorizeRequests().antMatchers("/report/search/**").access(getTimelogReportPermission());
	http.authorizeRequests().antMatchers("/report/loadFilter/**").access(getLoadFilterPermissions());
	http.authorizeRequests().antMatchers("/report/revenues/**").access(getAccountingReportPermission());
	http.authorizeRequests().antMatchers("/phases/**").access(adminAndManagerAccess());
	http.authorizeRequests().antMatchers("/activate/**").permitAll();

	http.formLogin().loginPage("/login").defaultSuccessUrl("/home").usernameParameter("username").passwordParameter("password");
	
	http.authorizeRequests().antMatchers("/**").permitAll().and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
	
	http.csrf().csrfTokenRepository(csrfTokenRepository());

	// logout management
	http.logout().invalidateHttpSession(true).clearAuthentication(true);
	http.exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler("/403"));
    }

    private String adminAccess() {
	return "hasRole('ROLE_ADMIN')";
    }

    private String adminAndManagerAccess() {
	return "hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')";
    }

    private String getAccountingReportPermission() {
	return "hasRole('ROLE_ADMIN') or hasRole('ROLE_ACCOUNTING') or hasRole('ROLE_MANAGER')";
    }

    private String getTimelogReportPermission() {
	return "hasRole('ROLE_ADMIN') or hasRole('ROLE_HR') or hasRole('ROLE_MANAGER')";
    }

    private String getTimeLogPermissions() {
	return "hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_HR') or hasRole('ROLE_ACCOUNTING') or hasRole('ROLE_MANAGER')";
    }

    private String getLoadFilterPermissions() {
	return "hasRole('ROLE_ADMIN') or hasRole('ROLE_HR') or hasRole('ROLE_ACCOUNTING') or hasRole('ROLE_MANAGER')";
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
	
	final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	authenticationProvider.setUserDetailsService(userDetailsService);
	authenticationProvider.setPasswordEncoder(passwordEncoder());
	return authenticationProvider;
    }

    private CsrfTokenRepository csrfTokenRepository() {
	HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	repository.setHeaderName("X-XSRF-TOKEN");
	return repository;
    }

}