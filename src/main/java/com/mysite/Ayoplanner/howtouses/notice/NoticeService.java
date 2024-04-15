package com.mysite.Ayoplanner.howtouses.notice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.DataNotFoundException;
import com.mysite.Ayoplanner.admin.Admin;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {
	
	private final NoticeRepository noticeRepository;
	
	private Specification<Notice> search(String kw){
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Notice> n, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				return cb.or(
						cb.like(n.get("noticeSubject"), "%" + kw + "%"), //제목
						cb.like(n.get("noticeContent"), "%" + kw + "%")); // 내용
			}
		};
	}
	
	public Page<Notice> getNoticeList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("noticeCreateDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Notice> spec = search(kw);
		return this.noticeRepository.findAll(spec, pageable);
	}
	
	public List<Notice> getNoticeList() {
		return this.noticeRepository.findAll();
	}
	
	public Notice getNotice(Integer noticeId) {
		Optional<Notice> notice = this.noticeRepository.findById(noticeId);
		if (notice.isPresent()) {
			return notice.get();
		} else {
			throw new DataNotFoundException("notice not found");
		}
	}
	
	public void noticeCreate(String noticeSubject, String noticeContent, Admin admin) {
		Notice n = new Notice();
		n.setNoticeSubject(noticeSubject);
		n.setNoticeContent(noticeContent);
		n.setNoticeCreateDate(LocalDateTime.now());
		n.setAuthor(admin);
		this.noticeRepository.save(n);
	}
	
	public void noticeModify(Notice notice, String noticeSubject, String noticeContent) {
		notice.setNoticeSubject(noticeSubject);
		notice.setNoticeContent(noticeContent);
		notice.setNoticeCreateDate(LocalDateTime.now());
		this.noticeRepository.save(notice);
	}
	
	public void noticeDelete(Notice notice) {
		this.noticeRepository.delete(notice);
	}
}
