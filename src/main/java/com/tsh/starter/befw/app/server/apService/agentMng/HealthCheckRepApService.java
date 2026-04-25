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
import com.tsh.starter.befw.app.server.interfaces.subscriber.SolaceTaskReceiver;
import com.tsh.starter.befw.lib.core.ApMessage;
import com.tsh.starter.befw.lib.core.data.constant.MsgRepStatCd;
import com.tsh.starter.befw.lib.core.data.orm.GnSolMsgRep.GnSolMsgRepAccess;
import com.tsh.starter.befw.lib.core.data.orm.GnSolMsgRep.GnSolMsgRepModel;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.interfaces.InterfaceType;
import com.tsh.starter.befw.lib.core.messaging.solace.outbound.SolaceMessagePublisher;
import com.tsh.starter.befw.lib.core.messaging.solace.vo.SolaceOutBoundMessage;
import com.tsh.starter.befw.lib.core.spec.ApMessageHead;
import com.tsh.starter.befw.lib.core.spec.constant.ApMessageList;
import com.tsh.starter.befw.lib.core.spec.constant.ApSystemList;
import com.tsh.starter.befw.lib.core.spec.in.HealthCheckRep;
import com.tsh.starter.befw.lib.core.spec.out.HealthCheckTriggerRep;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HealthCheckRepApService extends AbstractApService<ApProcessVo<HealthCheckRep.Body>, HealthCheckRep.Body> {

	@Autowired
	GnSolMsgRepAccess gnSolMsgRepAccess;

	@Autowired
	SolaceMessagePublisher solaceMessagePublisher;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	protected Class<? extends ApMessage> getIvoClass() {
		return HealthCheckRep.class;
	}

	@Override
	public ApMessageList getSupportedEvent() {
		return HealthCheckRep.eventNm;
	}

	@Override
	protected ApProcessVo<HealthCheckRep.Body> buildProcessVo(InterfaceType interfaceType, ApMessage ivo) {
		HealthCheckRep healthCheckRep = (HealthCheckRep)ivo;
		return new ApProcessVo<HealthCheckRep.Body>().init(interfaceType, healthCheckRep,
			healthCheckRep.getBody());
	}

	@Override
	protected void mainAction(ApProcessVo<HealthCheckRep.Body> procVo) throws Exception {

		HealthCheckRep.Body body = procVo.getReceiveMsgInfo().getBody();

		GnSolMsgRepModel searchModel = GnSolMsgRepModel.builder()
			.reqSrvNm(body.getReqSrvNm())
			.reqTraceId(body.getReqTraceId())
			.build();

		GnSolMsgRepModel model = this.gnSolMsgRepAccess.findByUk(GnSolMsgRepModel.UK01, searchModel);
		if (model == null) {
			log.error("get response but, fail to fetch reply info.");
			throw new Exception("");
		}

		model.setRepStatCd(MsgRepStatCd.Response);
		this.gnSolMsgRepAccess.update(model.getObjId(), model);
		log.info("update statCd into Response. objId:{}", model.getObjId());

		this.sendHealthCheckReply(procVo, model);

	}

	@Override
	protected ApiResponse<ApProcessVo<HealthCheckRep.Body>> resultAction(ApProcessVo<HealthCheckRep.Body> procVo) {
		return null;
	}

	private void sendHealthCheckReply(ApProcessVo<HealthCheckRep.Body> procVo, GnSolMsgRepModel model) throws
		JsonProcessingException, JCSMPException {

		HealthCheckRep.Body body = procVo.getReceiveMsgInfo().getBody();

		log.info("start reply action based on saved data.");

		Map<String, Object> map = new HashMap<>();
		map.put(SolaceTaskReceiver.PROP_EVENT_NM, procVo.getEventNm());
		map.put(SolaceTaskReceiver.PROP_SELECT_KEY, model.getSelectorKey());
		map.put(SolaceTaskReceiver.PROP_RESP_TOPIC, model.getRecvTopicNm());

		HealthCheckTriggerRep req = HealthCheckTriggerRep.builder()
			.head(
				ApMessageHead.builder()
					.traceId("NEW_TRACE_ID")
					.eventNm(ApMessageList.HealthCheckRep)
					.tgt(ApSystemList.AGENT)
					.src(ApSystemList.SERVER)
					.build()
			)
			.body(
				HealthCheckTriggerRep.Body.builder()
					.tenant("TSH")
					.userId(body.getUserId())
					.result(body.getResult())
					.build()
			)
			.build();
		String payload = objectMapper.writeValueAsString(req);

		SolaceOutBoundMessage msg = SolaceOutBoundMessage.builder()
			.eventNm(procVo.getEventNm())
			.destination(model.getRecvTopicNm())
			.payload(payload)
			.msgHeader(map)
			.build();

		this.solaceMessagePublisher.publishToTopic(msg);

	}

}
