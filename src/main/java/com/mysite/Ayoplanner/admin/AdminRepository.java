package com.mysite.Ayoplanner.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	Optional<Admin> findByAdminName(String adminName);
}
