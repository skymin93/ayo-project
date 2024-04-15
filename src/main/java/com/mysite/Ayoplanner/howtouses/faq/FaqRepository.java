package com.mysite.Ayoplanner.howtouses.faq;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.Ayoplanner.howtouses.notice.Notice;

public interface FaqRepository extends JpaRepository<Faq, Integer>{
    Notice findByFaqSubject(String faqSubject); 
    Notice findByFaqSubjectAndFaqContent(String faqSubject, String faqContent);
    List<Notice> findByFaqSubjectLike(String faqSubject);
    Page<Faq> findAll(Pageable pageable);
    Page<Faq> findAll(Specification<Faq> spec, Pageable pageable);
}
