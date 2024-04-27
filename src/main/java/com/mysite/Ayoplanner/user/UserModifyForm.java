package com.mysite.Ayoplanner.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyForm {

	@Column(unique = true)
	@Size(min = 2, max = 25)
	@NotBlank(message = "수정할 닉네임을 입력해주세요.")
	private String newUsername;

	@Column(unique = true)
	private String email;
}
