package com.mysite.Ayoplanner.sociallogin;

import java.util.Map;

import com.mysite.Ayoplanner.user.SiteUser;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {// 모든 프로바이더에서 공통으로 사용할 커스텀한 인증프로퍼티 객체
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String username;
	private String email;
	private String picture;
	private String sns;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String username, String email,
			String picture, String sns) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.username = username;
		this.email = email;
		this.picture = picture;
		this.sns = sns;
	}

	// OAuth2User에서 반환하는 사용자 정보는 Map
	// 따라서 값 하나하나를 변환해야 한다.
	public static OAuthAttributes of(String registrationId, String userNameAttributeName,
			Map<String, Object> attributes) {
		if (registrationId.equals("kakao")) {
			return ofKakao(userNameAttributeName, attributes);
		}
		return ofGoogle(userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder().username((String) attributes.get("name"))
				.email((String) attributes.get("email")).picture((String) attributes.get("picture")).sns("kakao")
				.attributes(attributes).nameAttributeKey(userNameAttributeName).build();
	}

	// 구글 생성자
	private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder().username((String) attributes.get("name"))
				.email((String) attributes.get("email")).picture((String) attributes.get("picture")).sns("google")
				.attributes(attributes).nameAttributeKey(userNameAttributeName).build();
	}

	public SiteUser toEntity() {
		return SiteUser.builder().username(username).email(email).picture(picture).sns(sns).build();
	}

	// 이제 localhost:8080/oauth2/authorization/google URL에 요청을 보내면 구글 인증으로 리다이렉트가 되면서
	// 사용자는 자신의 구글계정을 인증하게 된다.
}
