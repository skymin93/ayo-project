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

import com.mysite.Ayoplanner.snslogin.CustomOAuth2UserService;

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
    	
        public static final String FRONT_URL = "http://localhost:8080";
        
        // @Bean -> 해당 메소드의 리턴되는 오브젝트를 IoC로 등록해줌
        @Bean
    	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        	http
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.csrf(csrf -> csrf.disable())
			.headers((headers) -> headers
				.addHeaderWriter(new XFrameOptionsHeaderWriter(
					XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
			.formLogin((formLogin) -> formLogin //.formLogin 메서드 : 스프링 시큐리티의 로그인 설정 담당
					.loginPage("/login") //로그인 페이지의 URL
					.usernameParameter("email") //로그인 시 사용할 파라미터 이름
					.defaultSuccessUrl("/")) //로그인 성공 시에 이동할 페이지 루트URL(/)
			.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //로그아웃 URL
				.logoutSuccessUrl("/") //로그아웃이 성공하면 루트(/)페이지로 이동
				.invalidateHttpSession(true))//로그아웃 시 생성된 사용자 세션도 삭제하도록 처리
			.oauth2Login((oauth2Login) -> oauth2Login
					.loginPage("/oauth2/authorization")
					.defaultSuccessUrl("/")
				.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
				)
			;
    		return http.build();
        }

		@Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
        
        @Bean
        AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
    }
