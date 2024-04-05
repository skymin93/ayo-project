package com.mysite.Ayoplanner.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckPwdForm {

	@Column(unique = true)
	@NotEmpty(message = "비밀번호를 입력해주세요.")
	private String checkPassword;

}
