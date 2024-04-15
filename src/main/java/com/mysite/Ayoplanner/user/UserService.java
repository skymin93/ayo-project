package com.mysite.Ayoplanner.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SiteUser create(String username, String password, String email) 
	{
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		this.userRepository.save(user);
		return user;
	}
	
	public SiteUser getUser(String email)
	{
		Optional<SiteUser> siteUser = this.userRepository.findByUsername(email);
		if(siteUser.isPresent())
		{
			return siteUser.get();
		}
		else
		{
			throw new DataNotFoundException("siteuser not found");
		}
	}
    
	//마이페이지 - 회원 닉네임 수정
	public SiteUser updateUsername(Long id, String newUsername) {
		SiteUser user = userRepository.findById(id).orElse(null);
		if (user != null| user.getUsername()!=newUsername) {
			user.setUsername(newUsername);
			return userRepository.save(user);
		}
		else
		{
			throw new DataNotFoundException("siteuser not found");
		}
	}
	
	//마이페이지 - 회원 비밀번호 수정
	@Transactional
	public void updatePassword(String email, String currentPassword, String newPassword) {
		SiteUser user = validatePassword(email, currentPassword);
		user.updatePassword((passwordEncoder.encode(newPassword)));
//		userService.updatePassword(email, UpdatePasswordForm.getCurrentPassword(), UpdatePasswordForm.getNewPassword());
	}

	private SiteUser validatePassword(String email, String currentPassword) {
		return null;
	}

	//마이페이지 - 회원 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}