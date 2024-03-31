package com.mysite.Ayoplanner;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.Ayoplanner.howtouses.faq.Faq;
import com.mysite.Ayoplanner.howtouses.faq.FaqRepository;

@SpringBootTest
class AyoplannerApplicationTests {
	
	@Autowired
	private FaqRepository faqRepository;

	@Transactional
	@Test
	void testJpa() {
		Faq f1 = new Faq();
		f1.setFaqSubject("1번이다");
		f1.setFaqContent("1번 성공");
		f1.setFaqCreateDate(LocalDateTime.now());
		this.faqRepository.save(f1);
	}
}
