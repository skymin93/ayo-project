package com.mysite.Ayoplanner.community.post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.mysite.Ayoplanner.community.answer.Answer;
import com.mysite.Ayoplanner.community.category.Category;
import com.mysite.Ayoplanner.community.comment.Comment;
import com.mysite.Ayoplanner.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 200)
	private String subject;

	@Column(columnDefinition = "TEXT")
	private String content;

	private LocalDateTime createDate;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;// 여러개니까 List형태로

	@ManyToOne
	private SiteUser author;

	private LocalDateTime modifyDate;

	@ManyToMany
	Set<SiteUser> voter;

	@OneToMany(mappedBy = "post")
	private List<Comment> commentList;

	@ManyToOne // many posts can have one category
	private Category category;

	@Column(columnDefinition = "Integer default 0", nullable = false)
	private int countview;

}
