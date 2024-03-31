package com.mysite.Ayoplanner.howtouses.faq;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FaqService {
	
	private final FaqRepository faqRepository;
	
	public List<Faq> getList() {
		return this.faqRepository.findAll();
	}
}

