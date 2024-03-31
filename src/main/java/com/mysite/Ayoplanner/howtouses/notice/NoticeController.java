package com.mysite.Ayoplanner.howtouses.notice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class NoticeController {

	@GetMapping(value = "howtouses/notice/detail/{id}")
	public String noticedetail(Model model, @PathVariable("noticeId") Integer noticeId) {
		return "inquiry_detail";
	}
}
