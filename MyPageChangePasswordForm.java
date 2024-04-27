package com.mysite.Ayoplanner.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyPageChangePasswordForm {

	@NotEmpty(message = "현재 비밀번호는 필수 항목입니다.")
	private String currentPassword;

	@NotEmpty(message = "새 비밀번호는 필수 항목입니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
	private String newPassword;

}