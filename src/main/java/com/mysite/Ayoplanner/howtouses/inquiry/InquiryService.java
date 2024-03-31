package com.mysite.Ayoplanner.howtouses.inquiry;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquiryService {
	
	private final InquiryRepository inquiryRepository;
	
	public List<Inquiry> getList() {
		return this.inquiryRepository.findAll();
	}
}
