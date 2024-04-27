package com.mysite.Ayoplanner.community.post;

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

import com.mysite.Ayoplanner.community.answer.Answer;
import com.mysite.Ayoplanner.community.category.Category;
import com.mysite.Ayoplanner.community.category.CategoryService;
import com.mysite.Ayoplanner.exception.DataNotFoundException;
import com.mysite.Ayoplanner.exception.ErrorCode;
import com.mysite.Ayoplanner.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

	private final PostRepository postRepository;
	private final CategoryService categoryService;

	// 유저 개인별 질문 모음(질문자)
	public Page<Post> getPersonalPostListByPostAuthorId(int page, String kw, Long authorId) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 페이지 번호, 개수
		return postRepository.findAllByKeywordAndAuthorId(kw, authorId, pageable);
	}

	// 유저 개인별 질문 모음(답변자)
	public Page<Post> getPersonalPostListByAnswer_AuthorId(int page, String kw, Long answerAuthorId) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 페이지 번호, 개수
		return postRepository.findAllByKeywordAndAnswer_AuthorId(kw, answerAuthorId, pageable);
	}

	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	private Specification<Post> search(String kw, String categoryName) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Post> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복 제거
				Join<Post, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Post, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Post, Category> c = q.join("category", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.and(cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"), // 내용
						cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
						cb.like(u2.get("username"), "%" + kw + "%")), // 답변 작성자
						// and
						cb.like(c.get("name"), "%" + categoryName + "%")); // 카테고리 이름
			}
		};
	}

	public void create(String subject, String content, SiteUser user, Category category) {
		Post p = new Post();
		p.setSubject(subject);
		p.setContent(content);
		p.setCreateDate(LocalDateTime.now());
		p.setAuthor(user);
		p.setCategory(category);
		this.postRepository.save(p);
	}

	public void modify(Post post, String subject, String content, String categoryName) {
		post.setSubject(subject);
		post.setContent(content);
		Category category = categoryService.getCategory(categoryName);
		post.setCategory(category);
		post.setModifyDate(LocalDateTime.now());
		this.postRepository.save(post);
	}

	public void delete(Post post) {
		this.postRepository.delete(post);
	}

	public void vote(Post post, SiteUser siteUser) {
		post.getVoter().add(siteUser);
		this.postRepository.save(post);
	}

	public Page<Post> getList(int page, String kw, String categoryName) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Post> spec = search(kw, categoryName);
		return this.postRepository.findAll(spec, pageable);
	}

	public List<Post> getCurrentListByUser(String username, int num) {
		Pageable pageable = PageRequest.of(0, num);
		return postRepository.findCurrentQuestion(username, pageable);
	}

	public Post getPost(Integer id) {
		Optional<Post> post = this.postRepository.findById(id);
		if (post.isPresent()) {
			return post.get();
		} else {
			throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
		}
	}
	
	@Transactional
    public Post hitPost(Integer id) {
        Optional<Post> opost = this.postRepository.findById(id);
        if (opost.isPresent()) {
            Post post = opost.get();
            post.setHit(post.getHit() + 1);
            return post;
        } else {
            throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

	public Page<Post> getAllList(int page, String kw) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Specification<Post> spec = searchAllList(kw);
		return this.postRepository.findAll(spec, pageable);
	}

	private Specification<Post> searchAllList(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Post> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복 제거
				Join<Post, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Post, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
						cb.like(q.get("content"), "%" + kw + "%"), // 내용
						cb.like(u1.get("username"), "%" + kw + "%"), // 질문 작성자
						cb.like(a.get("content"), "%" + kw + "%"), // 답변 내용
						cb.like(u2.get("username"), "%" + kw + "%") // 답변 작성자
				);
			}
		};
	}

	public Long getPostCount(SiteUser author) {
		return postRepository.countByAuthor(author);
	}

	public List<Post> getPostLatestByUser(SiteUser author) {
		return postRepository.findByAuthorOrderByCreateDateDesc(author);
	}
}
