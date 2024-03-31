package com.mysite.Ayoplanner.howtouses.adminanswer;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.mysite.Ayoplanner.howtouses.inquiry.Inquiry;

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
public class AdminAnswer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerId;
	
	@Column(columnDefinition = "TEXT")
	private String answerContent;
	
	@CreatedDate
	private LocalDateTime answerCreateDate;
	
	@ManyToOne
	private Inquiry inquiry;	
}
