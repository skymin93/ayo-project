package com.mysite.Ayoplanner.community.comment;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    private static final String COMMENT_FORM = "comment_form";
    private static final String REDIRECT_POST_DETAIL = "redirect:/post/detail/%s";

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/create/post/{id}")
    public String createQuestionComment(CommentForm commentForm) {
        return COMMENT_FORM;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/post/{id}")
    public String createQuestionComment(@PathVariable("id") Integer id, @Valid CommentForm commentForm,
            BindingResult bindingResult, Principal principal) {
    	if (bindingResult.hasErrors()) {
            return COMMENT_FORM;
        }
        Post post = this.postService.getPost(id);
        SiteUser user = this.userService.getUser(principal.getName());
        Comment c = this.commentService.create(post, user, commentForm.getContent());
        return String.format(REDIRECT_POST_DETAIL, c.getPostId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modifyComment(CommentForm commentForm, @PathVariable("id") Integer id, Principal principal) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        commentForm.setContent(comment.getContent());
        return COMMENT_FORM;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyComment(@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal,
            @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return COMMENT_FORM;
        }
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        comment = this.commentService.modify(comment, commentForm.getContent());
        return String.format(REDIRECT_POST_DETAIL, comment.getPostId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String deleteComment(Principal principal, @PathVariable("id") Integer id) {
        Comment comment = this.commentService.getComment(id);
        if (!comment.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.commentService.delete(comment);
        return String.format(REDIRECT_POST_DETAIL, comment.getPostId());
    }
}
