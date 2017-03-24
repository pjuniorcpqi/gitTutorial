package com.cpqi.andes.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import com.cpqi.andes.model.Principal;
import com.cpqi.andes.model.User;
import com.cpqi.andes.service.UserService;
import com.cpqi.andes.viewmodel.TimeSheetViewModel;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
		implements MethodSecurityExpressionOperations {

	private Object filterObject;
	private Object returnObject;
	private Object target;

	// add your custom methods

	public CustomMethodSecurityExpressionRoot(Authentication authentication) {
		super(authentication);
	}

	public boolean isAdmin() {
		return hasRole("ADMIN");
	}

	public boolean isOwner(User user) {
		boolean isOwner = true;
		Long userId = user.getId();
		Principal principal = getPrincipal(authentication);
		if (principal != null) {
			Long userIdPrincipal = principal.getUserId();
			Long userIdClient = Long.valueOf(userId);
			if (userIdClient != null && userIdPrincipal != null && !userIdClient.equals(userIdPrincipal)) {
				isOwner = false;
			}
		}
		return isOwner;
	}

	public boolean isOwner(Long userId) {
		boolean isOwner = true;
		Principal principal = getPrincipal(authentication);
		if (principal != null) {
			Long userIdPrincipal = principal.getUserId();
			Long userIdClient = Long.valueOf(userId);
			if (userIdClient != null && userIdPrincipal != null && !userIdClient.equals(userIdPrincipal)) {
				isOwner = false;
			}
		}
		return isOwner;
	}

	public boolean isOwner(TimeSheetViewModel day) {
		boolean isOwner = true;
		Principal principal = getPrincipal(authentication);
		if (principal != null) {
			Long userIdPrincipal = principal.getUserId();
			Long userIdClient = day.getUserId();
			if (userIdClient != null && userIdPrincipal != null && !userIdClient.equals(userIdPrincipal)) {
				isOwner = false;
			}
		}
		return isOwner;
	}

	private Principal getPrincipal(Authentication authentication) {
		Object object = authentication.getPrincipal();
		Principal principal = null;
		if (authentication != null && object instanceof com.cpqi.andes.model.Principal) {
			principal = (Principal) object;
		}
		return principal;
	}

	@Override
	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	@Override
	public Object getFilterObject() {
		return filterObject;
	}

	@Override
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	@Override
	public Object getReturnObject() {
		return returnObject;
	}

	/**
	 * Sets the "this" property for use in expressions. Typically this will be
	 * the "this" property of the {@code JoinPoint} representing the method
	 * invocation which is being protected.
	 *
	 * @param target
	 *            the target object on which the method in is being invoked.
	 */
	void setThis(Object target) {
		this.target = target;
	}

	@Override
	public Object getThis() {
		return target;
	}
}