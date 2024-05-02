package com.e_commerce.service;

import com.e_commerce.dto.UserInfo;
import com.e_commerce.entity.Users;
import com.e_commerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.e_commerce.constant.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        verify(userRepository).save(any(Users.class));
    }


}