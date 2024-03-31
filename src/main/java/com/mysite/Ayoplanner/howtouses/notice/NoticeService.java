package com.mysite.Ayoplanner.howtouses.notice;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {
	
	private final NoticeRepository noticeRepository;
	
	public List<Notice> getList() {
		return this.noticeRepository.findAll();
	}
}

