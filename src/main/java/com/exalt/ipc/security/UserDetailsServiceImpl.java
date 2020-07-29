package com.exalt.ipc.security;

import com.exalt.ipc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
//for authorization
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private final UserService userService;

	@Autowired
	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		com.exalt.ipc.entities.User user = userService.getUserOptional(email).get();
		User.UserBuilder builder = null;
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		builder = User.withUsername(email);
		builder.password(user.getPassword());
		builder.authorities(user.getRole());
		return builder.build();
	}

}
