package com.mysite.Ayoplanner.user;

import java.io.UnsupportedEncodingException;

import com.mysite.Ayoplanner.exception.EmailException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailService {

	public MimeMessage createMessage(String to) throws UnsupportedEncodingException, MessagingException;

	public void sendSimpleMessage(String to, String pw) throws EmailException;
}
