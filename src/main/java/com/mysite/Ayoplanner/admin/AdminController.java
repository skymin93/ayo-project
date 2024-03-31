package com.mysite.Ayoplanner.admin;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final AdminService adminService;

	@GetMapping("/signup")
	public String signup(AdminCreateForm adminCreateForm) {
		return "admin_signup_form";
	}

	@PostMapping("/signup")
	public String signup(@Valid AdminCreateForm adminCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "admin_signup_form";
		}

		if (!adminCreateForm.getAdminPassword1().equals(adminCreateForm.getAdminPassword2())) {
			bindingResult.rejectValue("adminPassword2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "admin_signup_form";
		}

		try {
			adminService.create(adminCreateForm.getAdminName(), adminCreateForm.getAdminEmail(), adminCreateForm.getAdminPassword1(), adminCreateForm.getPosition());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "admin_signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "admin_signup_form";
		}

		return "redirect:/admin/login";
	}
	@GetMapping("/login")
	public String login() {
		return "admin_login_form";
	}
	@GetMapping("/support")
	public String support() {
		return "support";
	}
	
	@GetMapping("/")
	public String adminMain() {
		return "redirect:admin/login";
	}
}