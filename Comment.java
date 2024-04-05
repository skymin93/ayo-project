package com.mysite.Ayoplanner.community.comment;

import java.time.LocalDateTime;

import com.mysite.Ayoplanner.community.answer.Answer;
import com.mysite.Ayoplanner.community.post.Post;
import com.mysite.Ayoplanner.user.SiteUser;

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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private SiteUser author;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Answer answer;
    
    public Integer getPostId() {
        Integer result = null;
        if (this.post != null) {
            result = this.post.getId();
        } else if (this.answer != null) {
            result = this.answer.getPost().getId();
        }
        return result;
    }
}