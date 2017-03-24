package com.cpqi.andes.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class Principal extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	public Principal(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	private Long userId;

	private Boolean logged;

	private Boolean admin;

	private String message;

	private String accessLevel;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getLogged() {
		return logged;
	}

	public void setLogged(Boolean logged) {
		this.logged = logged;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter method to accessLevel.
	 *
	 * @return the accessLevel as String
	 */
	public String getAccessLevel() {
		return accessLevel;
	}

	/**
	 * Setter name to accessLevel.
	 *
	 * @param accessLevel
	 *            the accessLevel to set
	 */
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	/**
	 * Getter method to admin.
	 *
	 * @return the admin as Boolean
	 */
	public Boolean getAdmin() {
		return admin;
	}

	/**
	 * Setter name to admin.
	 *
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Principal [userId=" + userId + ", logged=" + logged + ", admin=" + admin + ", message=" + message
				+ ", accessLevel=" + accessLevel + "]";
	}
}
