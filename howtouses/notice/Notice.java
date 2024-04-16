package com.mysite.Ayoplanner.howtouses.notice;

import java.time.LocalDateTime;

import com.mysite.Ayoplanner.admin.Admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer noticeId;
	
	@Column(length = 200)
	private String noticeSubject;
	
	@Column(columnDefinition = "TEXT")
	private String noticeContent;
	
	private LocalDateTime noticeCreateDate;
	
	@ManyToOne
	private Admin author;
	
	private LocalDateTime modifyDate;
}
