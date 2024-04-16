package com.mysite.Ayoplanner.howtouses.inquire;

import java.time.LocalDateTime;
import java.util.List;

import com.mysite.Ayoplanner.howtouses.adminanswer.AdminAnswer;
import com.mysite.Ayoplanner.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Inquire {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inquireId;
	
	@Column(length = 200)
	private String inquireSubject;
	
	@Column(columnDefinition = "TEXT")
	private String inquireContent;
	
	private LocalDateTime inquireCreateDate;
	
	@OneToMany(mappedBy = "inquire", cascade = CascadeType.REMOVE)
	private List<AdminAnswer> adminAnswerList; 
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
}
