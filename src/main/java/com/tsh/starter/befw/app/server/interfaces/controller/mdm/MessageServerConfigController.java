package com.tsh.starter.befw.app.server.interfaces.controller.mdm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.app.server.ApProcessVo;
import com.tsh.starter.befw.app.server.apService.mdm.MessageServerConfigApService;
import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnRes;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.interfaces.InterfaceType;
import com.tsh.starter.befw.lib.core.spec.in.AddMsgServerInf;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller는 단순하게 요청과 응답만 정의
 * URI 매핑에관 중점
 * 단순하게 맞는 BIZ 서비스만 호출한다.
 */
@RestController
@RequestMapping("/api/mdm/server/config")
@Slf4j
@Validated
public class MessageServerConfigController {

	@Autowired
	MessageServerConfigApService messageServerConfigApService;

	@PostMapping
	public ApiResponse<GnMsgSrvConnRes> generateData(@Valid @RequestBody AddMsgServerInf ivo) {
		log.info("ivo: {}", ivo);

		ApProcessVo<AddMsgServerInf.Body> processVo = new ApProcessVo<AddMsgServerInf.Body>()
			.init(InterfaceType.REST, ivo, ivo.getBody());

		log.info("processVo: {}", processVo);

		return this.messageServerConfigApService.run(processVo);
	}

}
