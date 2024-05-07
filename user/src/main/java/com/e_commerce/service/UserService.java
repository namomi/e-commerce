package com.e_commerce.service;

import static com.e_commerce.exception.ErrorCode.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e_commerce.dto.UserInfo;
import com.e_commerce.entity.User;
import com.e_commerce.exception.CustomException;
import com.e_commerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void save(UserInfo userInfo) {
		validateDuplicateUser(userInfo);
		userRepository.save(User.toEntity(userInfo, passwordEncoder));
	}

	@Transactional
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

	private void validateDuplicateUser(UserInfo userInfo) {
		if (userRepository.existsByUsernameOrEmail(userInfo.username(), userInfo.email())) {
			throw new CustomException(DUPLICATE_USER);
		}
	}
}
