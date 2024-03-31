package com.mysite.Ayoplanner.howtouses.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.Ayoplanner.howtouses.adminanswer.AdminAnswer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Inquiry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inquiryId;
	
	@Column(length = 200)
	private String inquirySubject;
	
	@Column(columnDefinition = "TEXT")
	private String inquiryContent;
	
	private LocalDateTime inquiryCreateDate;
	
	@OneToMany(mappedBy = "inquiry", cascade = CascadeType.REMOVE)
	private List<AdminAnswer> adminAnswerList;
}
