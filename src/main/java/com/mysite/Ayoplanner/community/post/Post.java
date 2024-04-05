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
//springboot가 클래스를 Entity로 인식, 생성자를 만들어서 데이터 생성을 하고, 
//기본적으로 setter를 사용하지않는게 좋음(변경하려면 메소드 추가작성할것)
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=200)//컬럼에 들어가는 데이터길이제한
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private LocalDateTime createDate; //테이블명 create_date으로 나옴
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) 
	//1:n관계, 질문삭제하면 답변도 삭제되도록 CascadeType.REMOVE
	private List<Answer> answerList;//여러개니까 List형태로
	
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
	
	@ManyToMany
	Set<SiteUser> voter;
	
	@OneToMany(mappedBy = "post")
    private List<Comment> commentList;
	
	@ManyToOne//many posts can have one category
	private Category category;
	
	//조회수
	@Column(columnDefinition = "Integer default 0", nullable = false)
	private int countview;

	}
