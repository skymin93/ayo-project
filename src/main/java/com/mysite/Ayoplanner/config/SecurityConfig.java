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
        
        @Bean
    	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        	http
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.csrf(csrf -> csrf.disable())
			.headers((headers) -> headers
				.addHeaderWriter(new XFrameOptionsHeaderWriter(
					XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
			.formLogin((formLogin) -> formLogin 
					.loginPage("/login") 
					.usernameParameter("email") 
					.defaultSuccessUrl("/")) 
			.logout((logout) -> logout
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true))
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
