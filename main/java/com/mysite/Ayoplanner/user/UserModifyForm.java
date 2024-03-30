package com.mysite.Ayoplanner.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyForm {

	@NotEmpty(message = "수정할 닉네임을 입력해주세요.")
	private String newUsername;

}
