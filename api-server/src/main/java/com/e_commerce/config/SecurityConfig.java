package com.e_commerce.config;

import static com.e_commerce.exception.ErrorCode.*;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import com.e_commerce.jwt.CustomAuthenticationFilter;
import com.e_commerce.jwt.JwtTokenFilter;
import com.e_commerce.jwt.JwtTokenProvider;
import com.e_commerce.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;
	private final TokenService tokenService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration con) throws Exception {
		AuthenticationManager manager = con.getAuthenticationManager();
		ObjectMapper objectMapper = new ObjectMapper();
		JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider, userDetailsService);
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter(jwtTokenProvider, tokenService);
		filter.setCustomAuthenticationManager(manager);
		filter.setFilterProcessesUrl("/user/login");

		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("user/register", "/h2-console/**").permitAll()
				.requestMatchers("/register/**", "/address/**").authenticated()
				.anyRequest().authenticated()
			)
			.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
			.logout(logout -> logout
				.logoutUrl("/user/logout")
				.logoutSuccessHandler(
					(HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
						String token = jwtTokenProvider.extractTokenFromRequest(request);
						if (token != null && tokenService.isTokenActive(token)) {
							tokenService.markTokenInactive(token);
							response.setStatus(HttpServletResponse.SC_OK);
							response.getWriter().write("Logout successful");
						} else {
							response.setStatus(INVALID_OR_INACTIVE_TOKEN.status());
							response.setContentType("application/json");
							response.getWriter()
								.write(objectMapper.writeValueAsString(Map.of("error", ALREADY_LOGGED_IN.message())));
						}
					})
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID", "jwt")
				.clearAuthentication(true)
			)

			.headers(headers -> headers
				.addHeaderWriter(new XFrameOptionsHeaderWriter(
					XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
				))
			);

		return http.build();
	}
}
