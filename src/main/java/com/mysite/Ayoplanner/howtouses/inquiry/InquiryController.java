package com.mysite.Ayoplanner.howtouses.inquiry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class InquiryController {

	@GetMapping(value = "howtouses/inquiry/detail/{id}")
	public String inquirydetail(Model model, @PathVariable("inquiryId") Integer inquiryId) {
		return "inquiry_detail";
	}
	
}
