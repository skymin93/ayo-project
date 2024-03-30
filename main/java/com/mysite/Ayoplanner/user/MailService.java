package com.mysite.Ayoplanner.user;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	
	  //랜덤함수로 임시비밀번호 구문 만들기
	    public static String getCertificationNumber(){
	        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
	                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	        String str = "";

	        // 문자 배열 길이의 값을 랜덤으로 7개를 뽑아 구문을 작성함
	        int idx = 0;
	        for (int i = 0; i < 7; i++) {
	            idx = (int) (charSet.length * Math.random());
	            str += charSet[idx];
	        }
	        return str;
	    }
	    
		//메일 내용 생성, 비밀번호 재설정 이메일 인증번호 발송
		public MailDto createMailAndChangePassword(String email) {
			String str = getCertificationNumber();
			MailDto dto = new MailDto();
			dto.setAddress(email);
			dto.setTitle("[Ayoplanner] Ayoplanner 비밀번호 재설정 인증번호 안내 이메일입니다.");
			dto.setMessage("안녕하세요. Ayoplanner 비밀번호 재설정 인증번호 안내 관련 이메일입니다. " + " 회원님의 비밀번호 재설정 인증번호는 "
	                + str + " 입니다.");
	        return dto;
		}
		
	    // 메일보내기
	    public void mailSend(MailDto mailDto) {
	        System.out.println("전송 완료!");
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(mailDto.getAddress());
	        message.setSubject(mailDto.getTitle());
	        message.setText(mailDto.getMessage());
	        message.setFrom("보낸이@naver.com");
	        message.setReplyTo("보낸이@naver.com");
	        System.out.println("message"+message);
//	        MailSender.send(message);
	    }
	 }
