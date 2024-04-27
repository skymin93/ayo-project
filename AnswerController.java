package com.mysite.Ayoplanner.community.answer;

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
import org.springframework.web.server.ResponseStatusException;

import com.mysite.Ayoplanner.community.post.Post;
import com.mysite.Ayoplanner.community.post.PostService;
import com.mysite.Ayoplanner.user.SiteUser;
import com.mysite.Ayoplanner.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer") // url 프리픽스를 /answer로 고정
@RequiredArgsConstructor
@Controller
public class AnswerController {

	private final PostService postService;
	private final AnswerService answerService;
	private final UserService userService;

	// /answer/create/{id}와 같은 URL 요청 시 createAnswer 메서드가 호출되도록 @PostMapping으로 매핑한다.
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")

	public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm,
			BindingResult bindingResult, Principal principal) {
		Post post = this.postService.getPost(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (bindingResult.hasErrors()) {
			Page<Answer> answerPaging = this.answerService.getList(post, 0);
			model.addAttribute("answerPaging", answerPaging);
			model.addAttribute("post", post);
			return "post_detail";
		}
		Answer answer = this.answerService.create(post, answerForm.getContent(), siteUser);
		int page = post.getAnswerList().size() / 10;
		model.addAttribute("answerPage", page);
		return String.format("redirect:/post/detail/%s#answer_%s", answer.getPost().getId(), answer.getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
			@PathVariable("id") Integer id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/post/detail/%s#answer_%s", answer.getPost().getId(), answer.getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.answerService.delete(answer);
		return String.format("redirect:/post/detail/%s", answer.getPost().getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.answerService.vote(answer, siteUser);
		return String.format("redirect:/post/detail/%s#answer_%s", answer.getPost().getId(), answer.getId());
	}
}