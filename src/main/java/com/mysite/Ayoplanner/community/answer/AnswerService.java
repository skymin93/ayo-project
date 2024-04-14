package com.mysite.Ayoplanner.community.answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mysite.Ayoplanner.community.post.Post;
import com.mysite.Ayoplanner.exception.DataNotFoundException;
import com.mysite.Ayoplanner.exception.ErrorCode;
import com.mysite.Ayoplanner.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	private final AnswerRepository answerRepository;

	public Answer create(Post post, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setPost(post);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
		return answer;
	}

	public Answer getAnswer(Integer id) {
		Optional<Answer> answer = this.answerRepository.findById(id);
		if (answer.isPresent()) {
			return answer.get();
		} else {
			throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
		}
	}

	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		this.answerRepository.save(answer);
	}

	public void delete(Answer answer) {
		this.answerRepository.delete(answer);
	}

	public void vote(Answer answer, SiteUser siteUser) {
		answer.getVoter().add(siteUser);
		this.answerRepository.save(answer);
	}

	public Long getAnswerCount(SiteUser author) {
		return answerRepository.countByAuthor(author);
	}

	public List<Answer> getAnswerTop5LatestByUser(SiteUser user) {
		return answerRepository.findTop5ByAuthorOrderByCreateDateDesc(user);
	}

	public Page<Answer> getList(Post post, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("voter"));
		sorts.add(Sort.Order.asc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.answerRepository.findAllByPost(post, pageable);
	}

	public List<Answer> getCurrentListByUser(String username, int num) {
		Pageable pageable = PageRequest.of(0, num);
		return answerRepository.findCurrentAnswer(username, pageable);
	}
}
