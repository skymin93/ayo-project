package com.mysite.Ayoplanner.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	
	private final AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String adminEmail) throws UsernameNotFoundException {
		Optional<Admin> _admin = this.adminRepository.findByAdminEmail(adminEmail);
		if (_admin.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
		}
		Admin admin = _admin.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if ("admin".equals(adminEmail)) {
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		return new User(admin.getAdminEmail(), admin.getAdminPassword(), authorities);
	}
}
