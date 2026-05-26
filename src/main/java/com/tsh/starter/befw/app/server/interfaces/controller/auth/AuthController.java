package com.tsh.starter.befw.app.server.interfaces.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.lib.core.apService.auth.AuthService;
import com.tsh.starter.befw.lib.core.apService.auth.dto.LoginRequest;
import com.tsh.starter.befw.lib.core.apService.auth.dto.LoginResponse;
import com.tsh.starter.befw.lib.core.apService.auth.dto.RegisterRequest;
import com.tsh.starter.befw.lib.core.interfaces.rest.ApiResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Void> register(@RequestBody RegisterRequest request) {
		authService.register(request);
		return ApiResponse.noContent();
	}

	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(
		@RequestBody LoginRequest request,
		HttpServletResponse response
	) {
		LoginResponse loginResponse = authService.login(request);
		setRefreshTokenCookie(response, loginResponse.getRefreshToken());
		
		// Remove refreshToken from body as it's in cookie
		loginResponse.setRefreshToken(null);
		
		return ApiResponse.ok(loginResponse);
	}

	@PostMapping("/refresh")
	public ApiResponse<LoginResponse> refresh(
		HttpServletRequest request,
		HttpServletResponse response
	) {
		String refreshToken = getRefreshTokenFromCookie(request);
		LoginResponse loginResponse = authService.refresh(refreshToken);
		setRefreshTokenCookie(response, loginResponse.getRefreshToken());
		
		loginResponse.setRefreshToken(null);
		
		return ApiResponse.ok(loginResponse);
	}

	@PostMapping("/logout")
	public ApiResponse<Void> logout(
		HttpServletRequest request,
		HttpServletResponse response
	) {
		String refreshToken = getRefreshTokenFromCookie(request);
		authService.logout(refreshToken);
		
		// Clear cookie
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // Set to true in production
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		
		return ApiResponse.noContent();
	}

	private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(false); // Set to true in production
		cookie.setPath("/");
		cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
		response.addCookie(cookie);
	}

	private String getRefreshTokenFromCookie(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("refreshToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		throw new IllegalArgumentException("Refresh token not found");
	}
}
