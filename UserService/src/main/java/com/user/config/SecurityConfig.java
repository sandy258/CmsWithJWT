package com.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.user.security.JwtAuthenticationEntryPoint;
import com.user.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	
	@Autowired
	private UserDetailsService  detailsService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(c->c.disable())
					.authorizeHttpRequests(auth -> auth
							.requestMatchers("/v1/cms/**")
							.authenticated()
							.requestMatchers("/auth/**")
							.permitAll()
							.anyRequest()
							.authenticated())
					.exceptionHandling(ex-> ex.authenticationEntryPoint(authenticationEntryPoint))
					.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider  authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(detailsService);
		authenticationProvider.setPasswordEncoder(encoder);
		return authenticationProvider;
	}

}
