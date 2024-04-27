package com.mysite.Ayoplanner.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.sociallogin.PrincipalDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<SiteUser> _siteUser = this.userRepository.findByEmail(email);

		if (_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
		}
		SiteUser siteUser = _siteUser.get();
		return new PrincipalDetails(siteUser);
	}
}