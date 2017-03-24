package com.cpqi.andes.configuration;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import com.cpqi.andes.model.Principal;

public class CustomWebExpressionVoter extends WebExpressionVoter {

	@Override
	public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {

		int vote = super.vote(authentication, fi, attributes);

		if (ACCESS_GRANTED == vote) {
			vote = validateSameUser(fi.getHttpRequest(), authentication);
		}

		return vote;
	}

	private int validateSameUser(HttpServletRequest request, Authentication authentication) {
		int vote = 1;
		String userId = request.getParameter("userId");
		Principal principal = getPrincipal(authentication);
		Long userIdClient = null;
		if (principal != null) {
			Long userIdPrincipal = principal.getUserId();
			if (userId != null && !userId.equals("")) {
				userIdClient = Long.valueOf(userId);
			}
			if (userIdClient != null && userIdPrincipal != null && !userIdClient.equals(userIdPrincipal)) {
				vote = -1;
			}
		}
		return vote;

	}

	private Principal getPrincipal(Authentication authentication) {
		Object object = authentication.getPrincipal();
		Principal principal = null;
		if (authentication != null && object instanceof com.cpqi.andes.model.Principal) {
			principal = (Principal) object;
		}
		return principal;
	}

}
