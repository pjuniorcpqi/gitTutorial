package com.cpqi.andes.configuration;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;

import org.springframework.security.web.session.HttpSessionEventPublisher;

@WebListener
public class SessionListener extends HttpSessionEventPublisher {

	 @Override
	 public void sessionCreated(HttpSessionEvent httpSessionEvent) {
	  super.sessionCreated(httpSessionEvent);
	  httpSessionEvent.getSession().setMaxInactiveInterval(30 * 60);
	 }

	 @Override
	 public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
	  super.sessionDestroyed(httpSessionEvent);
	 }
}