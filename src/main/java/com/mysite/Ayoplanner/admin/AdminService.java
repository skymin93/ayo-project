package com.mysite.Ayoplanner.admin;

import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.Ayoplanner.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminService {
	
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;	
	
	public Admin create(String adminEmail, String adminPassword, String adminName, String position) {
		Admin admin = new Admin();
		admin.setAdminEmail(adminEmail);
		admin.setAdminPassword(passwordEncoder.encode(adminPassword));
		admin.setAdminName(adminName);
		admin.setPosition(position);
		this.adminRepository.save(admin);
		return admin;
	}
	
	public Admin getAdmin(String adminEmail) {
		Optional<Admin> admin = this.adminRepository.findByAdminEmail(adminEmail);
		if (admin.isPresent()) {
			return admin.get();
		} else {
			throw new DataNotFoundException("admin not found");
		}
	}
	
	private final JavaMailSender mailSender;

    public MailForm createMailForm(String adminEmail) {
        String adminPassword = createRandomPassword();
        updateTempPassword(adminPassword, adminEmail);

        MailForm mailForm = new MailForm();
        mailForm.setAddress(adminEmail);
        mailForm.setTitle("임시비밀번호 안내 메일입니다.");
        mailForm.setMessage("회원님의 임시 비밀번호는 " + adminPassword + "입니다. 감사합니다");

        return mailForm;
    }

    public String createRandomPassword() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append((int) (Math.random() * 10));
        }

        return sb.toString();
    }

    public void updateTempPassword(String adminPassword, String adminEmail) {
        Admin admin = adminRepository.findByAdminEmail(adminEmail).orElseThrow(() -> new DataNotFoundException("Admin not found with email: " + adminEmail));
        admin.setAdminPassword(passwordEncoder.encode(adminPassword));
        adminRepository.save(admin); // 실제 비밀번호를 저장
    }


    public void sendEmail(MailForm mailForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailForm.getAddress());
        message.setSubject(mailForm.getTitle());
        message.setText(mailForm.getMessage());
        message.setFrom("0101angela@gmail.com");
        message.setReplyTo("0101angela@gmail.com");
        mailSender.send(message);
    }
}
