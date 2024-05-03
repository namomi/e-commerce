package com.e_commerce.controller;

import com.e_commerce.dto.UserInfo;
import com.e_commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@PostMapping("/user/register")
	public void registerUser(@RequestBody UserInfo userInfo) {
		userService.save(userInfo);
	}
}
