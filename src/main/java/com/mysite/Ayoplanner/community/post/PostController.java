package com.mysite.Ayoplanner.community.post;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.Ayoplanner.community.answer.Answer;
import com.mysite.Ayoplanner.community.answer.AnswerForm;
import com.mysite.Ayoplanner.community.answer.AnswerService;
import com.mysite.Ayoplanner.community.category.Category;
import com.mysite.Ayoplanner.community.category.CategoryService;
import com.mysite.Ayoplanner.user.SiteUser;
import com.mysite.Ayoplanner.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;
	private final UserService userService;
	private final AnswerService answerService;
	private final CategoryService categoryService;

	@GetMapping("/post/list")
	public String getLatestPosts(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> latestPosts = postService.getAllList(page, kw);
		model.addAttribute("latestPosts", latestPosts.getContent());
		model.addAttribute("paging", latestPosts);
		model.addAttribute("kw", kw);
		return "post_list";
	}

	@GetMapping(value = "/post/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm,
			@RequestParam(value = "answerPage", defaultValue = "0") int answerPage) {
		Post post = this.postService.hitPost(id);
		Page<Answer> answerPaging = this.answerService.getList(post, answerPage);
		model.addAttribute("post", post);
		model.addAttribute("answerPaging", answerPaging);
		return "post_detail";
	}

	@GetMapping("/post/list/joinMember")
	public String joinMemberList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> paging = this.postService.getList(page, kw, "가입인사");
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "post_list_joinMember";
	}

	@GetMapping("/post/list/transportation")
	public String transportationList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> paging = this.postService.getList(page, kw, "교통");
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "post_list_transportation";
	}

	@GetMapping("/post/list/tripplan")
	public String tripplanList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> paging = this.postService.getList(page, kw, "여행일정");
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "post_list_tripplan";
	}

	@GetMapping("/post/list/city")
	public String cityList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> paging = this.postService.getList(page, kw, "도시별");
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "post_list_city";
	}

	@GetMapping("/post/list/expenses")
	public String expensesList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> paging = this.postService.getList(page, kw, "쇼핑경비환전");
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "post_list_expenses";
	}

	@GetMapping("/post/list/info")
	public String infoList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> paging = this.postService.getList(page, kw, "기타여행정보");
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "post_list_info";
	}

	@GetMapping("/post/list/hotel")
	public String hotelList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Post> paging = this.postService.getList(page, kw, "숙소");
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "post_list_hotel";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post/create")
	public String postCreate(Model model, PostForm postForm) {
		model.addAttribute("categoryList", categoryService.getList());
		return "post_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/post/create")
	public String createPost(Model model, @Valid PostForm postForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("categoryList", categoryService.getList());
			return "post_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		Category category = this.categoryService.getCategory(postForm.getCategory());
		this.postService.create(postForm.getSubject(), postForm.getContent(), siteUser, category);
		return "redirect:/post/list";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post/modify/{id}")
	public String postModify(PostForm postForm, @PathVariable("id") Integer id, Principal principal) {
		Post post = this.postService.getPost(id);
		if (!post.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다");
		}
		postForm.setSubject(post.getSubject());
		postForm.setContent(post.getContent());
		postForm.setCategory(post.getSubject());
		return "post_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/post/modify/{id}")
	public String postModify(@Valid PostForm postForm, BindingResult bindingResult, Principal principal,
			@PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "post_form";
		}
		Post post = this.postService.getPost(id);
		if (!post.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");
		}
		this.postService.modify(post, postForm.getSubject(), postForm.getContent(), postForm.getCategory());
		return String.format("redirect:/post/detail/%s", id);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		Post post = this.postService.getPost(id);
		if (!post.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.postService.delete(post);
		return "redirect:/";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/post/vote/{id}")
	public String postVote(Principal principal, @PathVariable("id") Integer id) {
		Post post = this.postService.getPost(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.postService.vote(post, siteUser);
		return String.format("redirect:/post/detail/%s", id);
	}
}
