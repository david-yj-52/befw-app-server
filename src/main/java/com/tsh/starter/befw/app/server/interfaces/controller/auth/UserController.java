package com.tsh.starter.befw.app.server.interfaces.controller.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.app.server.apService.cira.dto.UserSearchResponse;
import com.tsh.starter.befw.lib.core.apService.auth.UserService;
import com.tsh.starter.befw.lib.core.data.orm.usersAndPermissions.gsUser.GsUserAccess;
import com.tsh.starter.befw.lib.core.interfaces.rest.ApiResponse;
import com.tsh.starter.befw.lib.core.interfaces.rest.auth.BaseUserController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseUserController {

	private final GsUserAccess userAccess;

	public UserController(UserService userService, GsUserAccess userAccess) {
		super(userService);
		this.userAccess = userAccess;
	}

	@GetMapping("/search")
	public ApiResponse<List<UserSearchResponse>> searchUsers(@RequestParam String keyword) {
		List<UserSearchResponse> results = userAccess.searchByKeyword(keyword).stream()
			.map(u -> UserSearchResponse.builder()
				.id(u.getObjId())
				.name(u.getUserNm())
				.email(u.getEmail())
				.avatarUrl(u.getAvatarUrl())
				.build())
			.collect(Collectors.toList());
		return ApiResponse.ok(results);
	}
}
