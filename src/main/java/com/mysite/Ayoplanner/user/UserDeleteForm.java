package com.mysite.Ayoplanner.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteForm {
	@NotEmpty(message = "현재 비밀번호를 입력해야 합니다.")
	private String password;
}
