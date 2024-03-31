package com.mysite.Ayoplanner.howtouses.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.admin.Admin;
import com.mysite.Ayoplanner.howtouses.question.Question;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {

	private final AnswerRepository answerRepository;

	public void create(Question question, String content, Admin author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		this.answerRepository.save(answer);
	}
}
