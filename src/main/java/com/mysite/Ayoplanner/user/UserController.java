package com.mysite.Ayoplanner.user;

import java.security.Principal;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysite.Ayoplanner.community.answer.Answer;
import com.mysite.Ayoplanner.community.answer.AnswerService;
import com.mysite.Ayoplanner.community.post.Post;
import com.mysite.Ayoplanner.community.post.PostService;
import com.mysite.Ayoplanner.exception.DataNotFoundException;
import com.mysite.Ayoplanner.exception.EmailException;
import com.mysite.Ayoplanner.exception.ErrorCode;
import com.mysite.Ayoplanner.sociallogin.PrincipalDetails;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final PostService postService;
	private final AnswerService answerService;
	private final UserService userService;
	private final UserRepository userRepository;

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}

	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println("입력값 검증 중 에러 발생");
			return "signup_form";
		}
		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다");
			return "signup_form";
		}
		try {
			userService.create(userCreateForm.getEmail(), userCreateForm.getUsername(), userCreateForm.getPassword1());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다");
			return "signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "login_form";
	}

	@GetMapping("/tempPassword")
	public String tempPassword(TempPasswordForm tempPasswordForm) {
		return "TempPasswordForm";
	}

	@PostMapping("/tempPassword")
	public String sendTempPassword(@Valid TempPasswordForm tempPasswordForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "TempPasswordForm";
		}
		try {
			userService.modifyPassword(tempPasswordForm.getEmail());
		} catch (DataNotFoundException e) {
			e.printStackTrace();
			bindingResult.reject("emailNotFound", e.getMessage());// 이메일이db에없음
			return "TempPasswordForm";
		} catch (EmailException e) {
			e.printStackTrace();
			bindingResult.reject("sendEmailFail", e.getMessage());// 이메일전송실패
			return "TempPasswordForm";
		}
		return "redirect:/";
	}

	@GetMapping("/mypage")
	@PreAuthorize("isAuthenticated()")
	public String showmyPage(Model model, Principal principal) {
		SiteUser user = userService.getUser(principal.getName());

		if (user == null) {
			throw new DataNotFoundException(ErrorCode.USER_NOT_FOUND_BY_USERNAME);
		}
		model.addAttribute("user", user);
		
		Long postCount = postService.getPostCount(user);
		model.addAttribute("postCount", postCount);

		List<Post> postList = postService.getPostLatestByUser(user);
		model.addAttribute("postList", postList);

		Long answerCount = answerService.getAnswerCount(user);
		model.addAttribute("answerCount", answerCount);

		List<Answer> answerList = answerService.getAnswerLatestByUser(user);
		model.addAttribute("answerList", answerList);

		return "mypage";
	}
	
	@GetMapping("/mypage/checkPwdForm")
	public String checkPwdView() {
		return "checkPwdForm";
	}

	@GetMapping("mypage/checkPwd")
	@ResponseBody
	public boolean checkPassword(@AuthenticationPrincipal PrincipalDetails principal,
			@RequestParam("checkPassword") String checkPassword, Model model) {

		String userName = principal.getUsername();
		SiteUser checkName = userRepository.findByUsername(userName);

		System.out.println(checkName.getPassword());
		return userService.checkPassword(checkName, checkPassword);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage/profile")
	public String modifyInfo(Principal principal, Model model) {
		String username = principal.getName();
		SiteUser siteUser = userRepository.findByUsername(username);
		model.addAttribute("siteUser", siteUser);
		return "mypage_profile";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/mypage/profile")
	@Transactional
	public String changeUsername(Principal principal, @RequestParam("newUsername") String newUsername) {
		String username = principal.getName();
		userService.changeUsername(username, newUsername);
		return "redirect:/mypage";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage/changePassword")
	public String changePasswordForm(Model model) {
		model.addAttribute("passwordChangeForm", new MyPageChangePasswordForm());
		return "mypage_changePassword";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/mypage/changePassword")
	@Transactional
	public String changePassword(
			@ModelAttribute("passwordChangeForm") @Valid MyPageChangePasswordForm passwordChangeForm,
			BindingResult bindingResult, Principal principal, RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			System.out.println("입력값 검증 중 에러 발생");
			return "mypage_changePassword_form";
		}

		String username = principal.getName();
		userService.changePassword(username, passwordChangeForm.getCurrentPassword(),
				passwordChangeForm.getNewPassword());
		attributes.addFlashAttribute("message", "패스워드를 변경했습니다.");
		return "redirect:/mypage";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage/deleteUser")
	public String deleteUser(Model model) {
		model.addAttribute("userDeleteForm", new UserDeleteForm());
		return "mypage_deleteUser";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/mypage/deleteUser")
	public String deleteUser(@ModelAttribute("userDeleteForm") @Valid UserDeleteForm userDeleteForm,
			BindingResult bindingResult, Principal principal, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			System.out.println("입력값 검증 중 에러 발생");
			return "mypage_deleteUser";
		}

		String username = principal.getName();
		userService.deleteUser(username);
		redirectAttributes.addFlashAttribute("message", "회원 탈퇴되었습니다.");
		return "redirect:/";
	}
}
