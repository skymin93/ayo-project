package com.mysite.Ayoplanner.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyForm {
	
	@Column(unique = true)
	@Size(min = 3, max = 25)
	@NotBlank(message = "수정할 닉네임을 입력해주세요.")
	private String username;
	
    @Column(unique = true)
    private String email;
    
    @Column(unique = true)
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,16}",
			message = "비밀번호는 영문,숫자,특수문자 포함 8자리 이상이어야 합니다.")
	private String password;

}
