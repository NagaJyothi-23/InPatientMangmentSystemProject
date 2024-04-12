package com.admin.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.admin.entity.RegistrationForm;
import com.admin.exception.UsernameNotFoundException;
import com.admin.repository.RegistrationRepository;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {
	@Autowired
	private RegistrationRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<RegistrationForm> registrationForm = repository.findByEmail(email);
		return registrationForm.map(UserInfoUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("user not found " + email));

	}
}
