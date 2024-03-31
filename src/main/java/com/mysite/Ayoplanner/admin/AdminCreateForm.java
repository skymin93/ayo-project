package com.mysite.Ayoplanner.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateForm {
	@Size(min = 3, max = 25)
	@NotEmpty(message = "사용자 ID는 필수 항목입니다.")
	private String adminName;
	
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String adminPassword1;
	
	@NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
	private String adminPassword2;
	
	@NotEmpty(message = "이메일은 필수 항목입니다.")
	@Email
	private String adminEmail;
	
	@NotEmpty(message = "직책은 필수 항목입니다.")
	private String position;

}
