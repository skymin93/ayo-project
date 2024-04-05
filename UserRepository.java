package com.mysite.Ayoplanner.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//기본적인 CRUD 함수를 가지고 있음
//JpaRepository를 상속했기 때문에 @Repository 어노테이션 불필요
public interface UserRepository extends JpaRepository<SiteUser, Long> {//SiteUser의 기본키 타입은 Long
	SiteUser findByid(Long id);
	SiteUser findByemail(String email);
	SiteUser findByUsername(String username);
	Optional<SiteUser> findByEmail(String email);
	Optional<SiteUser> findByusername(String username);//사용자 username로 SiteUser엔티티를 조회하는 findByUsername메서드 추가
	
	}