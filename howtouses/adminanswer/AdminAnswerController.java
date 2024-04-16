package com.mysite.Ayoplanner.howtouses.adminanswer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.Ayoplanner.admin.Admin;
import com.mysite.Ayoplanner.admin.AdminService;
import com.mysite.Ayoplanner.howtouses.inquire.Inquire;
import com.mysite.Ayoplanner.howtouses.inquire.InquireService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/adminanswer")
@RequiredArgsConstructor
@Controller
public class AdminAnswerController {
	
	private final InquireService inquireService;
	private final AdminAnswerService adminAnswerService;
	private final AdminService adminService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAdminAnswer(Model model, @PathVariable("id") Integer inquireId, @Valid AdminAnswerForm adminAnswerForm, BindingResult bindingResult, Principal principal) {
		Inquire inquire = this.inquireService.getInquire(inquireId);
		Admin admin = this.adminService.getAdmin(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute("inquire", inquire);
			return "inquire_datail";
		}
		this.adminAnswerService.adminAnswerCreate(inquire, adminAnswerForm.getAnswerContent(), admin);
		return String.format("redirect:/inquire/detail/%s", inquireId);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AdminAnswerForm adminAnswerForm, @PathVariable("id") Integer answerId, Principal principal) {
		AdminAnswer adminAnswer = this.adminAnswerService.getAdminAnswer(answerId);
		if (!adminAnswer.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		adminAnswerForm.setAnswerContent(adminAnswer.getAnswerContent());
		return "admin_answer_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AdminAnswerForm adminAnswerForm, BindingResult bindingResult, @PathVariable("id") Integer answerId, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "admin_answer_form";
		}
		AdminAnswer adminAnswer = this.adminAnswerService.getAdminAnswer(answerId);
		if (!adminAnswer.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.adminAnswerService.answerModify(adminAnswer, adminAnswerForm.getAnswerContent());
		return String.format("redirect:/inquire/detail/%s", adminAnswer.getInquire().getInquireId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer answerId) {
		AdminAnswer adminAnswer = this.adminAnswerService.getAdminAnswer(answerId);
		if (!adminAnswer.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.adminAnswerService.answerDelete(adminAnswer);
		return String.format("redirect:/inquire/detail/%s", adminAnswer.getInquire().getInquireId());
	}
}
