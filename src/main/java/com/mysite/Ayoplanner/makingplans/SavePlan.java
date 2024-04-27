package com.mysite.Ayoplanner.makingplans;

import com.mysite.Ayoplanner.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SavePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "json")
    private String savePlanData;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private SiteUser user;
}

/**
 * 
 * 자바스크립트 객체 -> JSON 형태의 문자열 
 * JSON.strigify(자바스크립트 객체)
 * 
 * JSON 형태의 문자열 -> 자바스크립트 객체
 * JSON.parse(문자열)
 * 
 * 자바스크립트 API 호출 
 *
 
 */