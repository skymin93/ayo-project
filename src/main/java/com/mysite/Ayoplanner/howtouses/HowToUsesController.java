package com.mysite.Ayoplanner.howtouses;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysite.Ayoplanner.howtouses.faq.Faq;
import com.mysite.Ayoplanner.howtouses.faq.FaqService;
import com.mysite.Ayoplanner.howtouses.inquiry.Inquiry;
import com.mysite.Ayoplanner.howtouses.inquiry.InquiryService;
import com.mysite.Ayoplanner.howtouses.notice.Notice;
import com.mysite.Ayoplanner.howtouses.notice.NoticeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class HowToUsesController {

	private final FaqService faqService;
	private final InquiryService inquiryService;
	private final NoticeService noticeService;
	
	@GetMapping("/howtouses/list")
	public String list(Model model) {
		List<Faq> faqList = this.faqService.getList();
		model.addAttribute("faqList", faqList);
		List<Inquiry> inquiryList = this.inquiryService.getList();
		model.addAttribute("inquiryList", inquiryList);
		List<Notice> notice = this.noticeService.getList();
		model.addAttribute("notice", notice);
		return "howtouses_list";
	}
}
