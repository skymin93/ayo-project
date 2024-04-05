package com.mysite.Ayoplanner.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")    
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	private String email;
	
	@Size(min = 3, max = 25)//username은 입력받는 데이터의 길이가 3~25사이여야 한다는 검증조건 설정
	@NotEmpty(message = "사용자 닉네임은 필수 항목입니다.")
	private String username;
	
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String password1; //password1 : '비밀번호' 속성
	
	@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
	private String password2; //password2 : '비밀번호 확인' 속성
}