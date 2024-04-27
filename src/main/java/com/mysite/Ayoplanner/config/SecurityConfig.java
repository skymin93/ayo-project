package com.mysite.Ayoplanner.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.Ayoplanner.sociallogin.CustomOAuth2UserService;

//Oauth 로그인 진행 순서
//1. 인가 코드 발급(회원 인증)
//2. 엑세스 토큰 발급(접근 권한 부여)
//3. 액세스 토큰을 이용해 사용자 정보 불러오기
//4. 불러온 사용자 정보를 토대로 자동 회원가입/로그인 진행
//※ 소셜 플랫폼의 로그인과 프로젝트 앱의 로그인은 별개임!!

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

	/**
	 * @param http
	 * @return
	 * @throws Exception
	 */

	// @Bean -> 해당 메소드의 리턴되는 오브젝트를 IoC로 등록해줌
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
	    	.authorizeHttpRequests((authorize) -> authorize
	        .anyRequest().authenticated())
				.csrf(csrf -> csrf.disable())
				.headers((headers) -> headers
						.addHeaderWriter(new XFrameOptionsHeaderWriter(
							XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
				.formLogin(formLogin -> formLogin.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/"))
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/").invalidateHttpSession(true))
				.oauth2Login(oauth2Login -> oauth2Login.loginPage("/login")
						.defaultSuccessUrl("/")
						.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService)));
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
