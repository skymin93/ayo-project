package com.mysite.Ayoplanner.howtouses.inquire;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquireForm {
	@NotEmpty(message="제목은 필수 항목입니다.")
	@Size(max=200)
	private String inquireSubject;
	
	@NotEmpty(message="내용은 필수 항목입니다.")
	private String inquireContent;
}