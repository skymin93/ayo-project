package com.mysite.Ayoplanner.community.category;

import java.util.List;

import com.mysite.Ayoplanner.community.post.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
	//Category 엔티티를 추가하고 Question 엔티티에 Category 엔티티를 연결 -> 게시판 분류
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique = true)
	private String name;
	
	@OneToMany(mappedBy = "category")
    private List<Post> postList;
	
	}
