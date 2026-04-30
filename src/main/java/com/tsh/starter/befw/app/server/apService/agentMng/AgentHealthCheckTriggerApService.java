package com.tsh.starter.befw.app.server.apService.agentMng;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.JCSMPException;
import com.tsh.starter.befw.app.server.ApProcessVo;
import com.tsh.starter.befw.app.server.apService.AbstractApService;
import com.tsh.starter.befw.app.server.interfaces.subscriber.SolaceMessageInfoVo;
import com.tsh.starter.befw.app.server.interfaces.subscriber.SolaceTaskReceiver;
import com.tsh.starter.befw.lib.core.ApMessage;
import com.tsh.starter.befw.lib.core.data.constant.MsgRepStatCd;
import com.tsh.starter.befw.lib.core.data.orm.messageReply.gnSolMsgRep.GsSolMsgRepAccess;
import com.tsh.starter.befw.lib.core.data.orm.messageReply.gnSolMsgRep.GsSolMsgRepModel;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.interfaces.InterfaceType;
import com.tsh.starter.befw.lib.core.messaging.solace.outbound.SolaceMessagePublisher;
import com.tsh.starter.befw.lib.core.messaging.solace.vo.SolaceOutBoundMessage;
import com.tsh.starter.befw.lib.core.spec.ApMessageHead;
import com.tsh.starter.befw.lib.core.spec.constant.ApMessageList;
import com.tsh.starter.befw.lib.core.spec.constant.ApSystemList;
import com.tsh.starter.befw.lib.core.spec.in.HealthCheckTriggerReq;
import com.tsh.starter.befw.lib.core.spec.out.HealthCheckReq;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgentHealthCheckTriggerApService extends AbstractApService<ApProcessVo, HealthCheckTriggerReq.Body> {

	@Autowired
	SolaceMessagePublisher messagePublisher;

	@Autowired
	GsSolMsgRepAccess gsSolMsgRepAccess;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public ApMessageList getSupportedEvent() {
		return HealthCheckTriggerReq.eventNm;
	}

	@Override
	protected Class<? extends ApMessage> getIvoClass() {
		return HealthCheckTriggerReq.class;
	}

	@Override
	protected ApProcessVo<HealthCheckTriggerReq.Body> buildProcessVo(InterfaceType interfaceType, ApMessage ivo) {
		HealthCheckTriggerReq healthCheckTriggerReq = (HealthCheckTriggerReq)ivo;
		return new ApProcessVo<HealthCheckTriggerReq.Body>().init(interfaceType, healthCheckTriggerReq,
			healthCheckTriggerReq.getBody());
	}

	@Override
	protected void mainAction(ApProcessVo<HealthCheckTriggerReq.Body> procVo) {
		log.info("procVo:{}", procVo);
		this.registerCallBackSolMessage(procVo);

		try {
			this.sendHealthCheckRequestToAgent(procVo);
		} catch (Exception e) {
			log.error("fail to send health check req.");
			this.replyReqFailMessage(procVo);
		}

	}

	@Override
	protected ApiResponse<ApProcessVo> resultAction(ApProcessVo<HealthCheckTriggerReq.Body> procVo) {
		return null;
	}

	private void replyReqFailMessage(ApProcessVo<?> procVo) {
		log.info("send message back to let them know it is fail.");
	}

	private void sendHealthCheckRequestToAgent(ApProcessVo<?> procVo) throws JsonProcessingException, JCSMPException {
		log.info("start send h/c request to agent");

		Map<String, Object> map = new HashMap<>();
		map.put(SolaceTaskReceiver.PROP_EVENT_NM, procVo.getEventNm());

		HealthCheckReq req = HealthCheckReq.builder()
			.head(
				ApMessageHead.builder()
					.traceId("NEW_TRACE_ID")
					.eventNm(ApMessageList.HealthCheckReq)
					.tgt(ApSystemList.AGENT)
					.src(ApSystemList.SERVER)
					.build()
			)
			.body(
				HealthCheckReq.Body.builder()
					.tenant("TSH")
					.reqTraceId(procVo.getTraceId())
					.reqSrvNm(ApSystemList.SERVER.name())
					.userId(ApSystemList.SERVER.name())
					.build()
			)
			.build();
		String payload = objectMapper.writeValueAsString(req);

		SolaceOutBoundMessage msg = SolaceOutBoundMessage.builder()
			.eventNm(procVo.getEventNm())
			.destination("AGENT_TOPIC_NAME")
			.payload(payload)
			.msgHeader(map)
			.build();

		this.messagePublisher.publishToTopic(msg);
	}

	private void registerCallBackSolMessage(ApProcessVo<HealthCheckTriggerReq.Body> procVo) {

		log.info("start register solace call back message. traceId:{}", procVo.getTraceId());

		GsSolMsgRepModel storedModel = this.gsSolMsgRepAccess.create(this.generateModel(procVo), procVo);

		log.info("complete create model. objId:{}", storedModel.getObjId());

	}

	private GsSolMsgRepModel generateModel(ApProcessVo<?> procVo) {
		log.info("start generate model");

		SolaceMessageInfoVo infoVo = procVo.getMsgInfoVo();
		GsSolMsgRepModel model = GsSolMsgRepModel.builder()
			.reqSrvNm(ApSystemList.SERVER.name())
			.reqTraceId(procVo.getTraceId())
			.recvEvntNm(procVo.getEventNm())
			.recvTopicNm(infoVo.getResponseTopic())
			.selectorKey(infoVo.getSelectorKey())
			.repStatCd(MsgRepStatCd.Start)
			.build();

		return model;
	}

}
