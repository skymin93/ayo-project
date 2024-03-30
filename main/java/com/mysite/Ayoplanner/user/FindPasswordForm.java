package com.mysite.Ayoplanner.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPasswordForm {

	@NotEmpty(message = "이메일 주소")
	private String email;
}
