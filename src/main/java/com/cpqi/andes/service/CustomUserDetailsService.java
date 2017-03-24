package com.cpqi.andes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpqi.andes.model.Principal;
import com.cpqi.andes.model.User;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
	User user = userService.findByEmail(login);
	System.out.println("User : " + user);
	if (user == null) {
	    System.out.println("User not found");
	    throw new UsernameNotFoundException("Username not found");
	}
	Principal principal = new Principal(user.getEmail(), user.getPassword(), user.isActive(), true, true, true, getGrantedAuthorities(user));
	principal.setUserId(user.getId());
	principal.setAccessLevel("ROLE_" + user.getAccessLevel().getDescription().toUpperCase());
	return principal;
    }

    private List<GrantedAuthority> getGrantedAuthorities(User user) {
	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	System.out.println("UserProfile : " + user.getAccessLevel());
	authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getAccessLevel().getDescription().toUpperCase()));
	System.out.print("authorities :" + authorities);
	return authorities;
    }

}