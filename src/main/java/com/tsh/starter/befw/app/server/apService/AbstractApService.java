package com.tsh.starter.befw.app.server.apService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsh.starter.befw.app.server.ApProcessVo;
import com.tsh.starter.befw.app.server.interfaces.subscriber.SolaceMessageInfoVo;
import com.tsh.starter.befw.lib.core.ApMessage;
import com.tsh.starter.befw.lib.core.data.orm.common.tenant.TenantContext;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.interfaces.InterfaceType;
import com.tsh.starter.befw.lib.core.spec.ApMessageBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public abstract class AbstractApService<R, T extends ApMessageBody> implements ApService<R, T> {

	@Autowired
	private ObjectMapper objectMapper;

	protected abstract Class<? extends ApMessage> getIvoClass();

	@Override
	public void handle(String payload, InterfaceType interfaceType, SolaceMessageInfoVo infoVo) throws Exception {
		ApMessage ivo = objectMapper.readValue(payload, getIvoClass());
		// T 타입 추출 및 procVo 생성은 각 서비스에서 init 방식 활용
		ApProcessVo<T> procVo = buildProcessVo(interfaceType, ivo);
		procVo.setMsgInfoVo(infoVo);
		run(procVo);
	}

	protected abstract ApProcessVo<T> buildProcessVo(InterfaceType interfaceType, ApMessage ivo);

	// run 호출 → execute 자동 호출
	@Override
	public ApiResponse<R> run(ApProcessVo<T> procVo) {
		return execute(procVo);
	}

	// pre → main → post 순차 호출
	private ApiResponse<R> execute(ApProcessVo<T> procVo) {

		TenantContext.set(procVo.getTenant());

		preAction(procVo);
		procVo.setPreComp(true);

		mainAction(procVo);  // main은 abstract - 강제 구현
		procVo.setMainComp(true);

		postAction(procVo);
		procVo.setPostComp(true);

		ApiResponse<R> result = replyAction(procVo);
		procVo.setResponseComp(true);

		return result;
	}

	// 선택적 오버라이드
	protected void preAction(ApProcessVo<?> procVo) {
		log.info("[PRE] execute pre process");
	}

	// 강제 구현
	protected abstract void mainAction(ApProcessVo<T> procVo);

	// 선택적 오버라이드
	protected void postAction(ApProcessVo<T> procVo) {
		log.info("[POST] execute post process. evntName: {}", procVo.getEventNm());
	}

	// 강제 구현
	protected abstract ApiResponse<R> replyAction(ApProcessVo<T> procVo);

}
