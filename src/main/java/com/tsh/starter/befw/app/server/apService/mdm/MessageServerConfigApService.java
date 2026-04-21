package com.tsh.starter.befw.app.server.apService.mdm;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.app.server.ApProcessVo;
import com.tsh.starter.befw.app.server.apService.AbstractApService;
import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnRes;
import com.tsh.starter.befw.lib.core.data.orm.gnMsgSrvConn.GnMsgSrvConnAccess;
import com.tsh.starter.befw.lib.core.data.orm.gnMsgSrvConn.GnMsgSrvConnModel;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.spec.in.AddMsgServerInf;

import lombok.extern.slf4j.Slf4j;

/**
 * Ap Service
 * 1. Data Access 로직 처리
 * 2.
 */
@Service
@Slf4j
public class MessageServerConfigApService extends AbstractApService<GnMsgSrvConnRes, AddMsgServerInf.Body> {

	@Autowired
	GnMsgSrvConnAccess gnMsgSrvConnAccess;

	@Override
	protected void mainAction(ApProcessVo<AddMsgServerInf.Body> procVo) {
		log.info("proVo:{}", procVo);

		GnMsgSrvConnModel model = generateModel(procVo.getReceiveMsgInfo().getBody());
		gnMsgSrvConnAccess.upsert(procVo, model, GnMsgSrvConnModel.UK01);

	}

	private GnMsgSrvConnModel generateModel(AddMsgServerInf.Body body) {

		return GnMsgSrvConnModel.builder()
			.solNm(body.getSolNm())
			.env(body.getEnv())
			.host(body.getHost())
			.port(body.getPort())
			.connUser(body.getConUserId())
			.pwd(body.getPwd())
			.domain(body.getDomain())
			.defaultYn(body.getDefaultYn())
			.build();
	}

	@Override
	protected ApiResponse<GnMsgSrvConnRes> replyAction(ApProcessVo<AddMsgServerInf.Body> procVo) {
		log.info("response");
		return null;
	}

	@Override
	public CompletableFuture<ApiResponse<GnMsgSrvConnRes>> runAsync(ApProcessVo<AddMsgServerInf.Body> procVo) {
		return null;
	}
}
