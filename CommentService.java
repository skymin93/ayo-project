package com.mysite.Ayoplanner.community.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.community.post.Post;
import com.mysite.Ayoplanner.exception.DataNotFoundException;
import com.mysite.Ayoplanner.exception.ErrorCode;
import com.mysite.Ayoplanner.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
	@Service
	public class CommentService {

		private final CommentRepository commentRepository;

		public Comment create(Post post, SiteUser author, String content) {
			Comment c = new Comment();
			c.setPost(post);
			c.setAuthor(author);
			c.setContent(content);
			c.setCreateDate(LocalDateTime.now());
			c = this.commentRepository.save(c);
			return c;
		}

		public Comment getComment(Integer id) {
			Optional<Comment> comment = this.commentRepository.findById(id);
			if (comment.isPresent()) {
				return comment.get();
			} else {
				throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
			}
		}

		public Comment modify(Comment c, String content) {
			c.setContent(content);
			c.setModifyDate(LocalDateTime.now());
			c = this.commentRepository.save(c);
			return c;
		}

		public void delete(Comment c) {
			this.commentRepository.delete(c);
		}

		public List<Comment> getCurrentListByUser(String username, int num) {
			Pageable pageable = PageRequest.of(0, num);
			return commentRepository.findCurrentComment(username, pageable);
		}
}
