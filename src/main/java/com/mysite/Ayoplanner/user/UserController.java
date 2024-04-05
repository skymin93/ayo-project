package com.mysite.Ayoplanner.user;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.Ayoplanner.community.answer.AnswerService;
import com.mysite.Ayoplanner.community.comment.CommentService;
import com.mysite.Ayoplanner.community.post.PostService;
import com.mysite.Ayoplanner.exception.DataNotFoundException;
import com.mysite.Ayoplanner.exception.EmailException;
import com.mysite.Ayoplanner.mail.TempPasswordForm;
import com.mysite.Ayoplanner.snslogin.PrincipalDetails;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final PostService postService;
    private final UserService userService;
    private final AnswerService answerService;
    private final CommentService commentService;
    private final UserRepository userRepository;
    
	// /signup URL이 GET으로 요청되면 회원 가입을 위한 템플릿 렌더링
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	
	// /signup URL이 POST로 요청되면 회원가입 진행
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
			userService.create(userCreateForm.getEmail(), userCreateForm.getUsername(), userCreateForm.getPassword1());
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
	public String login(){
		return "login_form";
	}
	
	//비밀번호 찾기시, 임시 비밀번호 담긴 이메일 보내기
	@GetMapping("/tempPassword")
	public String tempPassword(TempPasswordForm tempPasswordForm) {
	return "TempPasswordForm";
	}

	@PostMapping("/tempPassword")
	public String sendTempPassword(@Valid TempPasswordForm tempPasswordForm,
	    BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	       return "TempPasswordForm";
	    }
	    try {
	    	userService.modifyPassword(tempPasswordForm.getEmail());
	    }catch (DataNotFoundException e) {
	    	e.printStackTrace();
	    	bindingResult.reject("emailNotFound", e.getMessage());//이메일이db에없음
	    	return "TempPasswordForm";
	    }catch (EmailException e) {
	    	e.printStackTrace();
	    	bindingResult.reject("sendEmailFail", e.getMessage());//이메일전송실패
	    	return "TempPasswordForm";
	    }
	    return "redirect:/";
	}
	
	//마이페이지 화면
		@PreAuthorize("isAuthenticated()") //ok
		@GetMapping("/mypage")
		public String profile(Model model,
		        @AuthenticationPrincipal PrincipalDetails principal) {
		        String username = principal.getUsername();
		        model.addAttribute("username", username);
		        model.addAttribute("postList",
		            postService.getCurrentListByUser(username, 10));
		        model.addAttribute("answerList",
		            answerService.getCurrentListByUser(username, 10));
		        model.addAttribute("commentList",
		            commentService.getCurrentListByUser(username, 10));
		        return "mypage";
		    }
	
		//회원정보수정전 비밀번호 체크 view
		@GetMapping("/mypage/checkPwdForm")
		public String checkPwdView() {
			return "checkPwdForm";
		}
		
		//회원정보수정전 비밀번호 체크
		@GetMapping("mypage/checkPwd")
		@ResponseBody
		public boolean checkPassword(@AuthenticationPrincipal PrincipalDetails principal,
	           @RequestParam("checkPassword") String checkPassword, Model model){
		
			String userName = principal.getUsername();
			SiteUser checkName = userRepository.findByUsername(userName);
				
			System.out.println(checkName.getPassword());
			return userService.checkPassword(checkName, checkPassword);
		}
		
		//로그인 되어있는 회원정보 수정 view
		/*@PreAuthorize("isAuthenticated()")
		@GetMapping("/mypage/profile")
		public String modifyInfo(@AuthenticationPrincipal PrincipalDetails principal) {
			return "mypage_profile";
		}*/
		
		//로그인 되어있는 회원정보 수정 view
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/mypage/profile") //userService에 있는 getUser를 이용하여 현재 로그인 되어있는 사용자의 정보를 가져온다
		public String modifyInfo(Principal principal, Model model) {
			String username = principal.getName();
			SiteUser siteUser = userRepository.findByUsername(username);
			model.addAttribute("siteUser", siteUser);
			return "mypage_profile";
		}

	//로그인 되어있는 회원정보 수정 
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/mypage/profile")
	@Transactional
	    public String modifyInfo(@Valid UserModifyForm userModifyForm, Model model) {
			model.addAttribute("user", userModifyForm);
			userService.modifyUser(userModifyForm);
			return "redirect:/mypage";
	}
	
	//회원탈퇴 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage/deleteUser")
	public String userDelete(UserDeleteForm userDeleteForm, Model model, Principal principal) {
		SiteUser siteUser = this.userService.getUser(principal.getName());
		model.addAttribute("siteUser", siteUser);
		return "mypage_deleteUser";
	}
	
	//계정삭제
	/*@PreAuthorize("isAuthenticated()")
	@GetMapping("mypage/deleteUser/{id}")
    public String deleteUser(Principal principal, @PathVariable String password) {
        SiteUser user = this.userService.getPassword(password);
        if(!user.getPassword().equals(principal.getName())) {
        	throw new
        	ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
        }
	this.userService.deleteUser(user);
		return "redirect:/";
	}*/
}
