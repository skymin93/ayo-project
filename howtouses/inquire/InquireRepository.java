package com.mysite.Ayoplanner.howtouses.inquire;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquireRepository extends JpaRepository<Inquire, Integer>{
	Inquire findByInquireSubject(String inquireSubject);
	Inquire findByInquireSubjectAndInquireContent(String inquireSubject, String inquireContent);
	List<Inquire> findByInquireSubjectLike(String InquireSubject);
	Page<Inquire> findAll(Pageable pageable);
	Page<Inquire> findAll(Specification<Inquire> spec, Pageable pageable);

}
