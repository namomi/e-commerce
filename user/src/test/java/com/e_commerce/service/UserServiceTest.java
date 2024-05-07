package com.e_commerce.service;

import static com.e_commerce.constant.Role.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.e_commerce.dto.UserInfo;
import com.e_commerce.entity.User;
import com.e_commerce.repository.UserRepository;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void registerSuccess() {
		// given
		UserInfo userInfo = new UserInfo("username", "password", "email@example.com", ADMIN);
		when(userRepository.existsByUsernameOrEmail(userInfo.username(), userInfo.email())).thenReturn(false);
		when(passwordEncoder.encode(userInfo.password())).thenReturn("encodedPassword");

		// when
		userService.save(userInfo);

		// then
		verify(userRepository).save(any(User.class));
	}

	@Test
	void deleteSuccess() throws Exception {
		//given
		Long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		//when
		userService.deleteUser(userId);

		//then
		verify(userRepository).findById(userId);
	}
}
