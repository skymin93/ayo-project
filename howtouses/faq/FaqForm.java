package com.mysite.Ayoplanner.howtouses.faq;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqForm {
	@NotEmpty(message="제목은 필수 항목입니다.")
	@Size(max=200)
	private String faqSubject;
	
	@NotEmpty(message="내용은 필수 항목입니다.")
	private String faqContent;
}
