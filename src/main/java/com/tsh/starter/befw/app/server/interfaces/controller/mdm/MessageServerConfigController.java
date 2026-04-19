package com.tsh.starter.befw.app.server.interfaces.controller.mdm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.schema.ServerSampleMessage;
import com.tsh.starter.befw.app.server.apService.mdm.MessageServerConfigApService;
import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnRes;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller는 단순하게 요청과 응답만 정의
 * URI 매핑에관 중점
 * 단순하게 맞는 BIZ 서비스만 호출한다.
 */
@RestController
@RequestMapping("/api/mdm/server/config")
@Slf4j
public class MessageServerConfigController {

	@Autowired
	MessageServerConfigApService messageServerConfigApService;

	@PostMapping
	public ApiResponse<GnMsgSrvConnRes> generateData(ServerSampleMessage ivo) {
		log.info("ivo: {}", ivo);
		return this.messageServerConfigApService.generateMessageServerData(ivo);
	}
}
