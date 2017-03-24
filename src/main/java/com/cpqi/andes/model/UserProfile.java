package com.cpqi.andes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_profiles")
public class UserProfile {

	@Id
	@GeneratedValue
	@Column(name = "id_user_profile")
	private long id;

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "id_profile", nullable = false)
	private Profile profile;

	/**
	 * Getter method to idUserProfile.
	 *
	 * @return the idUserProfile as long
	 */
	public long getIdUserProfile() {
		return id;
	}

	/**
	 * Setter name to idUserProfile.
	 *
	 * @param idUserProfile
	 *            the idUserProfile to set
	 */
	public void setIdUserProfile(long idUserProfile) {
		this.id = idUserProfile;
	}

	/**
	 * Getter method to user.
	 *
	 * @return the user as User
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter name to user.
	 *
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Getter method to profile.
	 *
	 * @return the profile as Profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * Setter name to profile.
	 *
	 * @param profile
	 *            the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", user=" + user + ", profile=" + profile + "]";
	}

}
