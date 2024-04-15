package com.mysite.Ayoplanner.howtouses.notice;

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
@RequestMapping("/notice")
@Controller
public class NoticeController {
	private final NoticeService noticeService;
	private final AdminService adminService;
	
	@GetMapping("/list")
	public String noticelist(Model model, @RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="kw", defaultValue = "") String kw) {
		Page<Notice> paging = this.noticeService.getNoticeList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "notice_list";
	}
	
	@GetMapping(value = "/detail/{id}")
	public String noticeDetail(Model model, @PathVariable("id") Integer noticeId) {
		Notice notice = this.noticeService.getNotice(noticeId);
		model.addAttribute("notice", notice);
		return "notice_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String noticeCreate(NoticeForm noticeForm) {
		return "notice_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String noticeCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "notice_form";
		}
		Admin admin = this.adminService.getAdmin(principal.getName());
		this.noticeService.noticeCreate(noticeForm.getNoticeSubject(), noticeForm.getNoticeContent(), admin);
		return "redirect:/notice/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String noticeModify(NoticeForm noticeForm, @PathVariable("id") Integer noticeId, Principal principal) {
		Notice notice = this.noticeService.getNotice(noticeId);
		if(!notice.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		noticeForm.setNoticeSubject(notice.getNoticeSubject());
		noticeForm.setNoticeContent(notice.getNoticeContent());
		return "notice_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String noticeModify(@Valid NoticeForm noticeForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer noticeId) {
		if (bindingResult.hasErrors()) {
			return "notice_form";
		}
		Notice notice = this.noticeService.getNotice(noticeId);
		if (!notice.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}
		this.noticeService.noticeModify(notice, noticeForm.getNoticeSubject(), noticeForm.getNoticeContent());
		return String.format("redirect:/notice/detail/%s", noticeId);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String noticeDelete(Principal principal, @PathVariable("id") Integer noticeId) {
		Notice notice = this.noticeService.getNotice(noticeId);
		if (!notice.getAuthor().getAdminEmail().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		this.noticeService.noticeDelete(notice);
		return "redirect:/notice/list";
	}
}

