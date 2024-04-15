package com.mysite.Ayoplanner.howtouses.faq;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.Ayoplanner.admin.Admin;
import com.mysite.Ayoplanner.admin.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/faq")
@Controller
public class FaqController {
	private final FaqService faqService;
	private final AdminService adminService;
	
	@GetMapping("/list")
	public String faqlist(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Faq> paging = this.faqService.getFaqList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "faq_list";
	}
	
	@GetMapping(value = "/detail/{id}")
	public String faqDetail(Model model, @PathVariable("id") Integer faqId) {
		Faq faq = this.faqService.getFaq(faqId);
		model.addAttribute("faq", faq);
		return "faq_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String faqCreate(FaqForm faqForm) {
		return "faq_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String faqCreate(@Valid FaqForm faqForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "faq_form";
		}
		Admin admin = this.adminService.getAdmin(principal.getName());
		this.faqService.faqCreate(faqForm.getFaqSubject(), faqForm.getFaqContent(), admin);
		return "redirect:/faq/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String faqModify(FaqForm faqForm, @PathVariable("id") Integer faqId, Principal principal) {
		Faq faq = this.faqService.getFaq(faqId);
		if(!faq.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		faqForm.setFaqSubject(faq.getFaqSubject());
		faqForm.setFaqContent(faq.getFaqContent());
		return "faq_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String faqModify(@Valid FaqForm faqForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer faqId) {
		if (bindingResult.hasErrors()) {
			return "faq_form";
		}
		Faq faq = this.faqService.getFaq(faqId);
		if (!faq.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.faqService.faqModify(faq, faqForm.getFaqSubject(), faqForm.getFaqContent());
		return String.format("redirect:/faq/detail/%s", faqId);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String faqDelete(Principal principal, @PathVariable("id") Integer faqId) {
		Faq faq = this.faqService.getFaq(faqId);
		if (!faq.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.faqService.faqDelete(faq);
		return "redirect:/faq/list";
	}
}
