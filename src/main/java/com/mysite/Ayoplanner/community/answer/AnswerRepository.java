package com.mysite.Ayoplanner.community.answer;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.Ayoplanner.community.post.Post;
import com.mysite.Ayoplanner.user.SiteUser;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	Long countByAuthor(SiteUser author);

	// 마이페이지에서 보여 줄 최신순 답변 5개
	List<Answer> findTop5ByAuthorOrderByCreateDateDesc(SiteUser user);

	Page<Answer> findAllByPost(Post post, Pageable pagealbe);

	@Query("select a " + "from Answer a " + "join SiteUser u on a.author=u " + "where u.username = :username "
			+ "order by a.createDate desc ")
	List<Answer> findCurrentAnswer(@Param("username") String username, Pageable pageable);
}
