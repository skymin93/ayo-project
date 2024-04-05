package com.mysite.Ayoplanner.user;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysite.Ayoplanner.CommonUtil;
import com.mysite.Ayoplanner.exception.DataNotFoundException;
import com.mysite.Ayoplanner.exception.EmailException;
import com.mysite.Ayoplanner.exception.ErrorCode;
import com.mysite.Ayoplanner.exception.UserDataIntegrityViolationException;
import com.mysite.Ayoplanner.mail.TempPasswordMail;

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
        Optional<SiteUser> siteUser =
            this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
        	throw new DataNotFoundException(ErrorCode.USER_NOT_FOUND_BY_USERNAME);
        }
    }
	
	//비밀번호찾기-임시비밀번호발송
	@Transactional
    public void modifyPassword(String email) throws EmailException {
        String tempPassword = commonUtil.createTempPassword();
        SiteUser user = userRepository.findByEmail(email)
            .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND_BY_EMAIL));
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);
        tempPasswordMail.sendSimpleMessage(email, tempPassword);
    }
    
	//마이페이지 - 비밀번호 체크 확인
	@ResponseBody
	public boolean checkPassword(SiteUser user, String checkPassword) {
		Optional<SiteUser> findUser = userRepository.findByEmail(user.getEmail());
		if(findUser == null) {
		     throw new IllegalStateException("없는 회원입니다.");
		}
		String realPassword = user.getPassword();
		boolean matches = passwordEncoder.matches(checkPassword, realPassword);
		System.out.println(matches);
		return matches;
	}
	
	//마이페이지 - 회원정보 수정
    public Long modifyUser(UserModifyForm userModifyForm) {
        SiteUser user = userRepository.findByemail(userModifyForm.getEmail());
        user.updateUsername(userModifyForm.getUsername());
        user.updatePassword(userModifyForm.getPassword());

        // 회원 비밀번호 수정을 위한 패스워드 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePw = encoder.encode(userModifyForm.getPassword());
        user.updatePassword(encodePw);

        userRepository.save(user);

        return user.getId();
    }
		
	//마이페이지 - 회원 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
	
	