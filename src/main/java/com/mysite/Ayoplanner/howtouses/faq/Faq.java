package com.mysite.Ayoplanner.howtouses.faq;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Faq {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer faqId;
	
	@Column(length = 200)
	private String faqSubject;
	
	@Column(columnDefinition = "TEXT")
	private String faqContent;
	
	private LocalDateTime faqCreateDate;
}
