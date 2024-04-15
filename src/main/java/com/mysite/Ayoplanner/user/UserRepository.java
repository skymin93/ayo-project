package com.mysite.Ayoplanner.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	SiteUser findByid(Long id);

	SiteUser findByemail(String email);

	SiteUser findByUsername(String username);

	Optional<SiteUser> findByEmail(String email);

	Optional<SiteUser> findByusername(String username);

	Optional<SiteUser> findBynewUsername(String newUsername);

	void deleteByUsername(String username);
}
