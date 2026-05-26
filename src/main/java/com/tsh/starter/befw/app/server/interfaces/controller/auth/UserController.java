package com.tsh.starter.befw.app.server.interfaces.controller.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.lib.core.apService.auth.UserService;
import com.tsh.starter.befw.lib.core.apService.auth.dto.UpdateProfileRequest;
import com.tsh.starter.befw.lib.core.apService.auth.dto.UserResponse;
import com.tsh.starter.befw.lib.core.interfaces.rest.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ApiResponse<UserResponse> getCurrentUser() {
		return ApiResponse.ok(userService.getCurrentUserProfile());
	}

	@PutMapping("/me")
	public ApiResponse<UserResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
		return ApiResponse.ok(userService.updateProfile(request));
	}
}
