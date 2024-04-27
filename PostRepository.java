package com.mysite.Ayoplanner.community.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mysite.Ayoplanner.user.SiteUser;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
	Post findBySubject(String subject); // findBy+엔티티의 속성이름(컬럼이름)  <-- JPA가 알아서
	Post findBySubjectAndContent(String subject, String content);
	List<Post> findBySubjectLike(String subject);
	Page<Post> findAll(Pageable pageable);
	Page<Post> findAll(Specification<Post> spec, Pageable pageable);
	 
	@Query("select q "
		        + "from Post q "
		        + "join SiteUser u on q.author=u "
		        + "where u.username = :username "
		        + "order by q.createDate desc ")
		    List<Post> findCurrentQuestion(@Param("username") String username,
		        Pageable pageable);

		    @Query("select "
		        + "distinct q "
		        + "from Post q "
		        + "left outer join SiteUser u1 on q.author=u1 "
		        + "left outer join Answer a on a.post=q "
		        + "left outer join SiteUser u2 on a.author=u2 "
		        + "where "
		        + "   q.subject like %:kw% "
		        + "   or q.content like %:kw% "
		        + "   or u1.username like %:kw% "
		        + "   or a.content like %:kw% "
		        + "   or u2.username like %:kw% ")
		    Page<Post> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
		    
		    
		    //질문자가 작성한 글만
		    @Query("select "
		    		+ "distinct q "
		    		+ "from Post q "
		    		+ "left outer join SiteUser u1 on q.author=u1 "
		    		+ "left outer join Answer a on a.post=q "
		    		+ "left outer join SiteUser u2 on a.author=u2 "
		    		+ "where "
		    		+ "   (q.author.id = :authorId) "
		    		+ "   and ( "
		    		+ "       q.subject like %:kw% "
		    		+ "       or q.content like %:kw% "
		    		+ "       or a.content like %:kw% "
		    		+ "       or u2.username like %:kw% "
		    		+ "   )")
			Page<Post> findAllByKeywordAndAuthorId(@Param("kw") String kw, @Param("authorId") Long authorId, Pageable pageable);
			
		    //질문자가 작성한 댓글만
		    @Query("select "
		    		+ "distinct q "
		    		+ "from Post q "
		    		+ "left outer join SiteUser u1 on q.author = u1 "
		    		+ "left outer join q.answerList a "
		    		+ "left outer join SiteUser u2 on a.author = u2 "
		    		+ "where "
		    		+ "   (u2.id = :authorId) "
		    		+ "   and ( "
		    		+ "       q.subject like %:kw% "
		    		+ "       or q.content like %:kw% "
		    		+ "       or u1.username like %:kw% "
		    		+ "       or a.content like %:kw% "
		    		+ "       or u2.username like %:kw% "
		    		+ "   )")
		    Page<Post> findAllByKeywordAndAnswer_AuthorId(@Param("kw") String kw, @Param("authorId") Long authorId, Pageable pageable);
			
		    Long countByAuthor(SiteUser author);
			List<Post> findByAuthorOrderByCreateDateDesc(SiteUser author);
			
}