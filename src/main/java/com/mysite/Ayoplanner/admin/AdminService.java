package com.mysite.Ayoplanner.admin;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service 
public class AdminService {

	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Admin create(String adminName, String adminEmail, String adminPassword, String position) {
		Admin admin = new Admin();
		admin.setAdminName(adminName);
		admin.setAdminEmail(adminEmail);
		admin.setAdminPassword(passwordEncoder.encode(adminPassword));
		admin.setPosition(position);
		this.adminRepository.save(admin);
		return admin;
	}
	
	public Admin getAdmin(String adminName) {
		Optional<Admin> admin = this.adminRepository.findByAdminName(adminName);
		if (admin.isPresent()) {
			return admin.get();
		} else {
			throw new DataNotFoundException("admin not found");
		}
	}
}
