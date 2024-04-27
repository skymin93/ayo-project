package com.mysite.Ayoplanner.howtouses.faq;

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
public class FaqService {
	private final FaqRepository faqRepository;
	
	private Specification<Faq> search(String kw){
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Faq> f, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				return cb.or(
						cb.like(f.get("faqSubject"), "%" + kw + "%"), //제목
						cb.like(f.get("faqContent"), "%" + kw + "%")); //내용
			}
		};
	}
	
	public Page<Faq> getFaqList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("faqCreateDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Faq> spec = search(kw);
		return this.faqRepository.findAll(spec, pageable);
	}
	
	public List<Faq> getFaqList() {
		return this.faqRepository.findAll();
	}
	
	public Faq getFaq(Integer faqId) {
		Optional<Faq> faq = this.faqRepository.findById(faqId);
		if (faq.isPresent()) {
			return faq.get();
		} else {
			throw new DataNotFoundException("faq not found");
		}
	}
	
	public void faqCreate(String faqSubject, String faqContent, Admin admin) {
		Faq f = new Faq();
		f.setFaqSubject(faqSubject);
		f.setFaqContent(faqContent);
		f.setFaqCreateDate(LocalDateTime.now());
		f.setAuthor(admin);
		this.faqRepository.save(f);
	}
	
	public void faqModify(Faq faq, String faqSubject, String faqContent) {
		faq.setFaqSubject(faqSubject);
		faq.setFaqContent(faqContent);
		faq.setFaqCreateDate(LocalDateTime.now());
		this.faqRepository.save(faq);
	}
	
	public void faqDelete(Faq faq) {
		this.faqRepository.delete(faq);
	}
}
