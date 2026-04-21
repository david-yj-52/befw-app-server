package com.tsh.starter.befw.app.server.actiavtor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.solacesystems.jcsmp.JCSMPSession;
import com.tsh.starter.befw.lib.core.config.MessagingProperties;
import com.tsh.starter.befw.lib.core.messaging.MessagingConfManager;
import com.tsh.starter.befw.lib.core.messaging.solace.SolaceInboundGateway;
import com.tsh.starter.befw.lib.core.messaging.solace.SolaceInboundManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppStarter implements ApplicationRunner {

	@Autowired
	MessagingConfManager messagingConfManager;

	@Autowired
	MessagingProperties messagingProperties;

	@Autowired
	SolaceInboundGateway solaceInboundGateway;

	@Autowired
	SolaceInboundManager solaceInboundManager;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		log.info("application started.");
		this.startConnectMessagingServer();

	}

	private void startConnectMessagingServer() {

		if (Boolean.parseBoolean(messagingProperties.getSolaceEnable())) {
			JCSMPSession solaceSession = this.messagingConfManager.getSolaceDefaultSession();

			if (Boolean.parseBoolean(messagingProperties.getSolaceSubEnable())) {
				this.solaceInboundGateway.setSession(solaceSession);
				this.solaceInboundManager.registerAll();
			}

			if (Boolean.parseBoolean(messagingProperties.getSolacePubEnable())) {
				// TODO Solace Publish init
			}
		}
	}

}
