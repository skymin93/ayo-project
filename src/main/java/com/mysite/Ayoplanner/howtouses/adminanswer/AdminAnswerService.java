package com.mysite.Ayoplanner.howtouses.adminanswer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.DataNotFoundException;
import com.mysite.Ayoplanner.admin.Admin;
import com.mysite.Ayoplanner.howtouses.inquire.Inquire;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminAnswerService {
	
	private final AdminAnswerRepository adminAnswerRepository;
	
	public void adminAnswerCreate(Inquire inquire, String answerContent, Admin author) {
		AdminAnswer adminAnswer = new AdminAnswer();
		adminAnswer.setAnswerContent(answerContent);
		adminAnswer.setAnswerCreateDate(LocalDateTime.now());
		adminAnswer.setInquire(inquire);
		adminAnswer.setAuthor(author);
		this.adminAnswerRepository.save(adminAnswer);
	}
	
	public AdminAnswer getAdminAnswer(Integer answerId) {
		Optional<AdminAnswer> adminAnswer = this.adminAnswerRepository.findById(answerId);
		if (adminAnswer.isPresent()) {
			return adminAnswer.get();
		} else {
			throw new DataNotFoundException("adminAnswer not found");
		}
	}
	
	public void answerModify(AdminAnswer adminAnswer, String answerContent) {
		adminAnswer.setAnswerContent(answerContent);
		adminAnswer.setAnswerCreateDate(LocalDateTime.now());
		this.adminAnswerRepository.save(adminAnswer);
	}
	
	public void answerDelete(AdminAnswer adminAnswer) {
		this.adminAnswerRepository.delete(adminAnswer);
	}
}
