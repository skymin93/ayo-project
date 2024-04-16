package com.mysite.Ayoplanner.howtouses.inquire;

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

import com.mysite.Ayoplanner.howtouses.adminanswer.AdminAnswerForm;
import com.mysite.Ayoplanner.user.SiteUser;
import com.mysite.Ayoplanner.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/inquire")
@RequiredArgsConstructor
@Controller
public class InquireController {
	
	private final InquireService inquireService;
	private final UserService userService;
	
	@GetMapping("/list")
	public String inquirelist(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Inquire> paging = this.inquireService.getInquireList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "inquire_list";
	}
	
	@GetMapping(value = "/detail/{id}")
	public String inquireDetail(Model model, @PathVariable("id") Integer inquireId, AdminAnswerForm answerForm) {
		Inquire inquire = this.inquireService.getInquire(inquireId);
		model.addAttribute("inquire", inquire);
		return "inquire_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String inquireCreate(InquireForm inquireForm) {
		return "inquire_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String inquireCreate(@Valid InquireForm inquireForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "inquire_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.inquireService.inquireCreate(inquireForm.getInquireSubject(), inquireForm.getInquireContent(), siteUser);
		return "redirect:/inquire/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String inquireModify(InquireForm inquireForm, @PathVariable("id") Integer inquireId, Principal principal) {
		Inquire inquire = this.inquireService.getInquire(inquireId);
		if(!inquire.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		inquireForm.setInquireSubject(inquire.getInquireSubject());
		inquireForm.setInquireContent(inquire.getInquireContent());
		return "inquire_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String inquireModify(@Valid InquireForm inquireForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer inquireId) {
		if (bindingResult.hasErrors()) {
			return "inquire_form";
		}
		Inquire inquire = this.inquireService.getInquire(inquireId);
		if (!inquire.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.inquireService.inquireModify(inquire, inquireForm.getInquireSubject(), inquireForm.getInquireContent());
		return String.format("redirect:/inquire/detail/%s", inquireId);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String inquireDelete(Principal principal, @PathVariable("id") Integer inquireId) {
		Inquire inquire = this.inquireService.getInquire(inquireId);
		if (!inquire.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.inquireService.inquireDelete(inquire);
		return "redirect:/inquire/list";
	}
}
