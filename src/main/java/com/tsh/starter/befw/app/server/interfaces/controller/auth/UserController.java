package com.tsh.starter.befw.app.server.interfaces.controller.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.lib.core.apService.auth.UserService;
import com.tsh.starter.befw.lib.core.interfaces.rest.auth.BaseUserController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseUserController {

	public UserController(UserService userService) {
		super(userService);
	}

	// 서비스 특화 로직이 필요한 경우 아래 hook 메서드를 오버라이드 하세요.
}
