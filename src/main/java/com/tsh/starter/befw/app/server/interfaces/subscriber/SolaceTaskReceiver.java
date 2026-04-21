package com.tsh.starter.befw.app.server.interfaces.subscriber;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.JCSMPException;
import com.tsh.starter.befw.lib.core.messaging.solace.inbound.SolaceMessageReceiver;
import com.tsh.starter.befw.lib.core.messaging.solace.util.SolaceQueueDiscovery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolaceTaskReceiver implements SolaceMessageReceiver {

	@Autowired
	SolaceQueueDiscovery solaceQueueDiscovery;

	@Override
	public List<String> getQueueNames() {
		return solaceQueueDiscovery.findQueuesByPattern("TET.*");
	}

	@Override
	public EndpointProperties getEndpointProperties() {
		EndpointProperties props = new EndpointProperties();
		props.setPermission(EndpointProperties.PERMISSION_CONSUME);
		props.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE); // 커스텀
		return props;
	}

	@Override
	public void onMessage(BytesXMLMessage message) throws Exception {
		// Tomcat Controller와 동일하게 그냥 동기 호출
		String payload = extractPayload(message);

		// TODO Call ApService
		// 리턴 → Gateway ACK 전송

	}

	@Override
	public void onException(JCSMPException ex) {
		log.error("[AgentTask] FlowReceiver exception", ex);
	}
}