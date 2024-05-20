package com.e_commerce.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.dto.UserInfo;
import com.e_commerce.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "유저 등록 및 탈퇴 API", description = "유저를 등록 및 탈퇴할 수 있습니다.")
public class UserController {

	private final UserService userService;

	@PostMapping("/user/register")
	@Operation(summary = "register user", description = "유저를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "This is an ID or email that has already been subscribed."),
	})
	public void registerUser(@RequestBody
							@Positive(message = "유저정보는 필수입니다..")
							UserInfo userInfo) {
		userService.save(userInfo);
	}

	@DeleteMapping("/user/delete{userId}")
	@Operation(summary = "delete user", description = "유저를 탈퇴합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "This is an ID or email that has already been subscribed."),
	})
	public void deleteUser(@PathVariable
				@Positive(message = "user id는 필수입니다.")
				@Schema(description = "userId", example = "1")
				Long userId) {
		userService.deleteUser(userId);
	}
}
