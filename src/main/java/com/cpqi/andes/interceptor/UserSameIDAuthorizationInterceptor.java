package com.cpqi.andes.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cpqi.andes.model.Principal;

public class UserSameIDAuthorizationInterceptor extends HandlerInterceptorAdapter {

	@Override
	public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException, IOException {

		String userId = request.getParameter("userId");
		Principal principal = getPrincipal(request);

		if (principal != null) {
			Long userIdPrincipal = principal.getUserId();
			Long userIdClient = Long.valueOf(userId);
			return userIdPrincipal.equals(userIdClient);
		}
		handleNotAuthorized(request, response, handler);
		return false;
	}

	private Principal getPrincipal(HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) request
				.getUserPrincipal();
		Principal principal = null;
		if (authenticationToken != null) {
			principal = (Principal) authenticationToken.getPrincipal();
		}
		return principal;
	}

	protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException, IOException {

		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

}
