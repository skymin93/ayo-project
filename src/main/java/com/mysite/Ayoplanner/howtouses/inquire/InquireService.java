package com.mysite.Ayoplanner.howtouses.inquire;

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
import com.mysite.Ayoplanner.howtouses.adminanswer.AdminAnswer;
import com.mysite.Ayoplanner.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InquireService {
	
	private final InquireRepository inquireRepository;
	
	private Specification<Inquire> search(String kw){
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Inquire> i, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				Join<Inquire, SiteUser> u = i.join("author", JoinType.LEFT);
				Join<Inquire, AdminAnswer> a1 = i.join("adminAnswerList", JoinType.LEFT);
				return cb.or(cb.like(i.get("inquireSubject"), "%" + kw + "%"), //제목
						cb.like(i.get("inquireContent"), "%" + kw + "%"), // 내용
						cb.like(u.get("username"), "%" + kw + "%"), // 질문 작성자
						cb.like(a1.get("answerContent"), "%" + kw + "%")); // 답변 내용
			}
		};
	}
	
	public Page<Inquire> getInquireList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("inquireCreateDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Inquire> spec = search(kw);
		return this.inquireRepository.findAll(spec, pageable);
	}
	
	public List<Inquire> getInquireList(){
		return this.inquireRepository.findAll();
	}
	
	public Inquire getInquire(Integer inquireId) {
		Optional<Inquire> inquire = this.inquireRepository.findById(inquireId);
		if (inquire.isPresent()) {
			return inquire.get();
		} else {
			throw new DataNotFoundException("inquire not found");
		}
	}
	
	public void inquireCreate(String inquireSubject, String inquireContent, SiteUser user) {
		Inquire i = new Inquire();
		i.setInquireSubject(inquireSubject);
		i.setInquireContent(inquireContent);
		i.setInquireCreateDate(LocalDateTime.now());
		i.setAuthor(user);
		this.inquireRepository.save(i);
	}
	
	public void inquireModify(Inquire inquire, String inquireSubject, String inquireContent) {
		inquire.setInquireSubject(inquireSubject);
		inquire.setInquireContent(inquireContent);
		inquire.setInquireCreateDate(LocalDateTime.now());
		this.inquireRepository.save(inquire);
	}
	
	public void inquireDelete(Inquire inquire) {
		this.inquireRepository.delete(inquire);
	}
}

