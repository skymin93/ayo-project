package com.mysite.Ayoplanner.community.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.DataNotFoundException;
import com.mysite.Ayoplanner.community.answer.Answer;
import com.mysite.Ayoplanner.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	public Page<Question> getList(int page, String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.questionRepository.findAllByKeyword(kw, pageable);
	}
	
	public Question getQuestion(Integer id) {//id값으로 질문 데이터 조회
		Optional<Question> question = this.questionRepository.findById(id);
		
		//setCountview로 조회수값변경
		//question1 : 값을 저장하고 다시 리턴해서 기존값에서 +1 하여 조회수 늘림
		if(question.isPresent()) {
			Question question1 = question.get();
			question1.setCountview(question1.getCountview() + 1);
			this.questionRepository.save(question1);
			return question.get();
		
		}else {
			throw new DataNotFoundException("question not found");
			//DataNotFoundException 클래스는 추가로 만들어야 컴파일 오류가 발생하지 않음
		}
	}

	public void create(String subject, String content, SiteUser user, String Questioncategory) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(user);
		q.setCategory(Questioncategory); //카테고리추가
		this.questionRepository.save(q);
	}

	
	public void modify(Question question, String subject, String content, String category)
	{
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		question.setCategory(category); //카테고리수정
		this.questionRepository.save(question);
	}
	
	public void delete(Question question) {
		this.questionRepository.delete(question);
	}
	
	public void vote(Question question, SiteUser siteUser) {
		question.getVoter().add(siteUser);
		this.questionRepository.save(question);
	}
	
	private Specification<Question> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true);
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw +"%"), //제목
						cb.like(q.get("content"), "%" + kw +"%"), //내용
						cb.like(u1.get("username"), "%" + kw + "%"), //질문 작성자 
						cb.like(a.get("content"), "%" + kw +"%"), //답변 내용
						cb.like(u2.get("username"), "%" + kw +"%")); //답변 작성자
			}
		};
	}
}
