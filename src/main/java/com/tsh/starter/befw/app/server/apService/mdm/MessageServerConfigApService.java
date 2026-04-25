package com.tsh.starter.befw.app.server.apService.mdm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.solacesystems.jcsmp.JCSMPException;
import com.tsh.starter.befw.app.server.ApProcessVo;
import com.tsh.starter.befw.app.server.apService.AbstractApService;
import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnRes;
import com.tsh.starter.befw.app.server.interfaces.subscriber.SolaceMessageInfoVo;
import com.tsh.starter.befw.app.server.interfaces.subscriber.SolaceTaskReceiver;
import com.tsh.starter.befw.lib.core.ApMessage;
import com.tsh.starter.befw.lib.core.data.orm.gnMsgSrvConn.GnMsgSrvConnAccess;
import com.tsh.starter.befw.lib.core.data.orm.gnMsgSrvConn.GnMsgSrvConnModel;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.interfaces.InterfaceType;
import com.tsh.starter.befw.lib.core.messaging.solace.outbound.SolaceMessagePublisher;
import com.tsh.starter.befw.lib.core.messaging.solace.vo.SolaceOutBoundMessage;
import com.tsh.starter.befw.lib.core.spec.constant.ApMessageList;
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

	@Autowired
	SolaceMessagePublisher publisher;

	@Override
	public ApMessageList getSupportedEvent() {
		return AddMsgServerInf.eventNm;  // ← 담당 이벤트
	}

	@Override
	protected Class<? extends ApMessage> getIvoClass() {
		return AddMsgServerInf.class;  // ← 역직렬화 타입
	}

	@Override
	protected ApProcessVo<AddMsgServerInf.Body> buildProcessVo(InterfaceType interfaceType, ApMessage ivo) {
		AddMsgServerInf addMsgServerInf = (AddMsgServerInf)ivo;
		return new ApProcessVo<AddMsgServerInf.Body>()
			.init(interfaceType, addMsgServerInf, addMsgServerInf.getBody());
	}

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
	protected ApiResponse<GnMsgSrvConnRes> resultAction(ApProcessVo<AddMsgServerInf.Body> procVo) {

		SolaceMessageInfoVo msgInfo = procVo.getMsgInfoVo();
		String selectorKey = msgInfo.getSelectorKey();
		String responseTopicName = msgInfo.getResponseTopic();

		String response = "{\n"
			+ "  \"head\": {\n"
			+ "    \"src\": \"SERVER\",\n"
			+ "    \"tgt\": \"UI\",\n"
			+ "    \"traceId\": \"string\",\n"
			+ "    \"eventNm\": \"InitializeData\"\n"
			+ "  },\n"
			+ "  \"body\": {\n"
			+ "    \"result\": \"SUCCESS\"\n"
			+ "  }\n"
			+ "}";

		if (StringUtils.hasText(selectorKey) && StringUtils.hasText(responseTopicName)) {

			log.info("response required.");
			Map<String, Object> map = new HashMap<>();
			map.put(SolaceTaskReceiver.PROP_EVENT_NM, procVo.getEventNm());
			map.put(SolaceTaskReceiver.PROP_SELECT_KEY, selectorKey);
			map.put(SolaceTaskReceiver.PROP_RESP_TOPIC, responseTopicName);

			SolaceOutBoundMessage msg = SolaceOutBoundMessage.builder()
				.eventNm(procVo.getEventNm())
				.destination(responseTopicName)
				.payload(response)
				.msgHeader(map)
				.build();

			try {
				this.publisher.publishToTopic(msg);
			} catch (JCSMPException e) {
				throw new RuntimeException(e);
			}

		}

		// TODO Response object definition required.
		return null;
	}

}
