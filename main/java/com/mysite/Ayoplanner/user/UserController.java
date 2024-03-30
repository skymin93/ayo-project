package com.mysite.Ayoplanner.user;

import java.security.Principal;

import org.aspectj.weaver.MemberUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

	private final UserRepository userRepository;
	private final UserService userService;
	private final MailService mailService;
	private final PasswordEncoder passwordEncoder;
	
	// user/signup URL이 GET으로 요청되면 회원 가입을 위한 템플릿 렌더링
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	
	// /user/signup URL이 POST로 요청되면 회원가입 진행
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			System.out.println("입력값 검증 중 에러 발생");
			return "signup_form";
		}
		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2()))
		{
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다");
			return "signup_form";
		}
		try
		{
			userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getEmail());
		} catch (DataIntegrityViolationException e)
		{
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다");
			return "signup_form";
		} catch (Exception e)
		{
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		return "redirect:/";
	}
	
	//로그인 화면
	@GetMapping("/login")
	public String login()
	{
		return "login_form";
	}
	
	//마이페이지 화면
	@PreAuthorize("isAuthenticated()") //ok
	@GetMapping("/myInfo")
	public String userInfo(Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		model.addAttribute("siteUser", siteUser);
		return "myInfo";
	}
	
	//로그인 되어있는 회원정보 닉네임(프로필설정화면) //ok
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myInfo/profile") //userService에 있는 getUser를 이용하여 현재 로그인 되어있는 사용자의 정보를 가져온다
	public String userProfile(UserModifyForm userModifyForm, Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		model.addAttribute("siteUser", siteUser);
		return "myInfo_profile";
		//model.addAttribute로 html에서 siteUser의 변수를 가져올 수 있다.
	}

	//로그인 되어있는 회원 닉네임 수정
	@PreAuthorize("isAuthenticated()")
	@PutMapping("/myInfo/profile")
	    public SiteUser updateUsername(@PathVariable Long id, @RequestParam String newUsername) {
	        return userService.updateUsername(id, newUsername);
	    }
	
	//로그인 - 비밀번호 찾기 화면
	// /findPassword url에 접속하면 비밀번호를 찾을 수 있는 폼을 보여주기 위해 findPassword메서드 생성
	@GetMapping("/findPassword")
	public String findPassword(FindPasswordForm findPasswordForm) {
		return "findPassword";
	}
	
	//비밀번호 찾기시, 임시 비밀번호 담긴 이메일 보내기
	@Transactional
	@PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("email") String email){
        MailDto dto = mailService.createMailAndChangePassword(email);
        mailService.mailSend(dto);

        return "/user/login";
    }
	
	//마이페이지 - 로그인 되어있는 회원 비밀번호 수정(비밀번호 변경 화면)
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myInfo/updatePassword") //userService에 있는 getUser를 이용하여 현재 로그인 되어있는 사용자의 정보를 가져온다
	public String userInfo(UpdatePasswordForm updatePasswordForm, Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		model.addAttribute("siteUser", siteUser);
		return "myInfo_updatePassword";
	}
	
	//마이페이지 - 로그인 되어있는 회원 비밀번호 수정
	@PutMapping("/myInfo/updatePassword")
    public ResponseEntity<String> updatePassword(@PathVariable Long id,
                                                  @RequestParam String currentPassword,
                                                  @RequestParam String newPassword) {
        SiteUser user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
	
	//회원탈퇴 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/myInfo/deleteUser")
	public String userDelete(UserDeleteForm userDeleteForm, Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		model.addAttribute("siteUser", siteUser);
		return "myInfo_deleteUser";
	}
	
	//계정삭제
	@DeleteMapping("myInfo/deleteUser/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
//	@PreAuthorize("isAuthenticated()")
//	@PostMapping("/myInfo/deleteUser")
//	public String userDelete(UserDeleteForm userDeleteForm, Principal principal) {
//		SiteUser siteUser = this.userService.getUser(principal.getName());
//		this.userService.delete(siteUser, userDeleteForm.getPassword());
//		return "redirect:/user/myInfo/deleteUser";
//	}
}
