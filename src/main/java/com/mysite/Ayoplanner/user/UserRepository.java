package com.mysite.Ayoplanner.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {//SiteUser의 기본키 타입은 Long
	Optional<SiteUser> findById(long id);
	Optional<SiteUser> findByUsername(String username);//사용자 username로 SiteUser엔티티를 조회하는 findByUsername메서드 추가
	Optional<SiteUser> findByPassword (String password);
	Optional<SiteUser> findByEmail(String email);
	}