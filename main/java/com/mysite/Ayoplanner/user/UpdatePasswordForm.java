package com.mysite.Ayoplanner.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordForm {//비밀번호 변경에 필요한 request 객체

	//DB에는 암호화된 password가 저장되어있다.
	//사용자에게 기존 pw와 새로운 pw를 입력받는다.
	//기존 pw가 현재 사용자의 pw와 일치한다면 해당 회원의 pw를 새로운 pw로 변경한다.
	//일치하지 않는다면 오류를 발생시킨다.
	
	@NotEmpty
	private String currentPassword;
	
	@NotEmpty
	@Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}",
		message = "영문,숫자,특수문자 포함 8자리 이상")
	private String newPassword;
}
