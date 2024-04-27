package com.mysite.Ayoplanner.user;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.Ayoplanner.CommonUtil;
import com.mysite.Ayoplanner.exception.DataNotFoundException;
import com.mysite.Ayoplanner.exception.EmailException;
import com.mysite.Ayoplanner.exception.ErrorCode;
import com.mysite.Ayoplanner.exception.UserDataIntegrityViolationException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CommonUtil commonUtil;
	private final TempPasswordMail tempPasswordMail;

	public SiteUser create(String email, String username, String password) {
		SiteUser user = new SiteUser();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		try {
			this.userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new UserDataIntegrityViolationException(ErrorCode.SIGN_UP_FAIL);
		}
		return user;
	}

	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException(ErrorCode.USER_NOT_FOUND_BY_USERNAME);
		}
	}

	// 비밀번호찾기-임시비밀번호발송
	@Transactional
	public void modifyPassword(String email) throws EmailException {
		String tempPassword = commonUtil.createTempPassword();
		SiteUser user = userRepository.findByEmail(email)
				.orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
		user.setPassword(passwordEncoder.encode(tempPassword));
		userRepository.save(user);
		tempPasswordMail.sendSimpleMessage(email, tempPassword);
	}

	// 마이페이지 - 비밀번호 체크 확인
	@ResponseBody
	public boolean checkPassword(SiteUser user, String checkPassword) {
		Optional<SiteUser> findUser = userRepository.findByEmail(user.getEmail());
		if (findUser == null) {
			throw new IllegalStateException("없는 회원입니다.");
		}
		String realPassword = user.getPassword();
		boolean matches = passwordEncoder.matches(checkPassword, realPassword);
		System.out.println(matches);
		return matches;
	}

	// 마이페이지 - 회원정보 닉네임 수정
	@Transactional
	public void changeUsername(String username, String newUsername) {
		SiteUser user = userRepository.findByUsername(username);
		if (user == null) {
			throw new IllegalArgumentException("해당 사용자가 없습니다.");
		}

		Optional<SiteUser> existingUser = userRepository.findBynewUsername(newUsername);
		if (existingUser.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
		}
		user.setUsername(newUsername);
		userRepository.save(user);
	}

	// 마이페이지 - 회원정보 비밀번호 수정
	@ResponseBody
	public void changePassword(String username, String currentPassword, String newPassword) {
		Optional<SiteUser> optionalUser = userRepository.findByusername(username);
		if (optionalUser.isPresent()) {
			SiteUser user = optionalUser.get();
			String encodedCurrentPassword = user.getPassword();

			if (!passwordEncoder.matches(currentPassword, encodedCurrentPassword)) {
				throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
			}

			String encodedNewPassword = passwordEncoder.encode(newPassword);
			user.setPassword(encodedNewPassword);
			userRepository.save(user);
		} else {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}
	}

	// 마이페이지 - 회원 삭제
	@Transactional
	public void deleteUser(String username) {
		userRepository.deleteByUsername(username);
	}

}
