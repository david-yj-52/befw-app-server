package com.tsh.starter.befw.app.server.actiavtor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.tsh.starter.befw.lib.core.messaging.MessagingConfManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppStarter implements ApplicationRunner {

	@Autowired
	MessagingConfManager messagingConfManager;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		log.info("application started.");
		this.startConnectMessagingServer();

	}

	private void startConnectMessagingServer() {
		this.messagingConfManager.init();
	}

}
