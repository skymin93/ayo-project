package com.mysite.Ayoplanner;

import lombok.Getter;

@Getter //UserRole의 ADMIN과 USER 상수는 값을 변경할 필요가 없으므로 @Setter없이 @Getter만 사용
public enum UserRole { //enum자료형(열거 자료형)으로 작성
	ADMIN("ROLE_ADMIN, ROLE_USER"), //관리자 ADMIN, 사용자 USER 상수 생성
	USER("ROLE_USER"); //ADMIN은 'ROLE_ADMIN', USER는 'ROLE_USER'라는 값 부여
	
	UserRole(String value) {
		this.value = value;
	}
	
	private String value;
}