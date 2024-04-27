package com.mysite.Ayoplanner.howtouses.notice;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Notice findByNoticeSubject(String noticeSubject); 
    Notice findByNoticeSubjectAndNoticeContent(String noticeSubject, String noticeContent);
    List<Notice> findByNoticeSubjectLike(String noticeSubject);
    Page<Notice> findAll(Pageable pageable);
    Page<Notice> findAll(Specification<Notice> spec, Pageable pageable);
}