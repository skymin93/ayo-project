package com.mysite.Ayoplanner.howtouses.faq;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FaqController {
	
	@GetMapping(value = "howtouses/faq/detail/{id}")
	public String faqdetail(Model model, @PathVariable("faqId") Integer faqId) {
		return "faq_detail";
	}

}
