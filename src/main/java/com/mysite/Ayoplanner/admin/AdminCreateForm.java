package com.mysite.Ayoplanner.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreateForm {

	@NotEmpty(message = "이메일은 필수 항목입니다.")
	@Email
	private String adminEmail;
	
	@Size(min = 8, max = 20, message = "8자 이상 20자 이내로 작성 가능합니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
	@NotEmpty(message = "비밀번호는 필수 항목입니다.")
	private String adminPassword1;
	
	@Size(min = 8, max = 20, message = "8자 이상 20자 이내로 작성 가능합니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
	@NotEmpty(message = "비밀번번 확인은 필수 항목입니다.")
	private String adminPassword2;
	
	@NotEmpty(message = "이름은 필수 항목입니다.")
	private String adminName;
	
	@NotEmpty(message = "직책은 필수 항목입니다.")
	private String position;

}
