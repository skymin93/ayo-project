package com.mysite.Ayoplanner.admin;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final AdminService adminService;
	
	@GetMapping("/signup")
	public String adminSignup(AdminCreateForm adminCreateForm) {
		return "admin_signup_form";
	}
	
	@PostMapping("/signup")
	public String adminSignup(@Valid AdminCreateForm adminCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "admin_signup_form";
		}
		
		if (!adminCreateForm.getAdminPassword1().equals(adminCreateForm.getAdminPassword2())) {
			bindingResult.rejectValue("adminPassword2", "passwordIncorrect", "2개의 비밀번호가 일치하지 않습니다.");
			return "admin_signup_form";
		}
		
		try {
			adminService.create(adminCreateForm.getAdminEmail(), adminCreateForm.getAdminPassword1(), adminCreateForm.getAdminName(), adminCreateForm.getPosition());
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "admin_signup_form";
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "admin_signup_form";
		}
			
		return "redirect:/admin/login";
	}
	
	@GetMapping("/login")
	public String adminLogin() {
		return "admin_login_form";
	}
	
	@GetMapping("/success")
	public String adminLoginSuccess() {
		return "admin_success";
	}
	
	@PreAuthorize("isAnonymous()")
    @GetMapping("/forgotPassword")
    public String findPassword() {

        return "admin_forgot_password";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam String adminEmail) {
        System.out.println("email  : " + adminEmail);
        MailForm mailForm = adminService.createMailForm(adminEmail);
        adminService.sendEmail(mailForm);

        return "redirect:/admin/login";
    }
}