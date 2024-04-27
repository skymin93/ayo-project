package com.mysite.Ayoplanner.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SiteUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
	@Column(unique = true)
	private String email;

	@Size(min = 2, max = 25)
	@Column(unique = true)
	private String username;

	private String newUsername;

	private String password;

	private String sns;

	private String picture;

	public void updateUsername(String newUsername) {
		this.username = newUsername;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public SiteUser update(String username, String picture) {
		this.username = username;
		this.picture = picture;

		return this;
	}

	@Builder
	public SiteUser(String username, String email, String sns, String picture) {
		this.username = username;
		this.email = email;
		this.sns = sns;
		this.picture = picture;
	}
}