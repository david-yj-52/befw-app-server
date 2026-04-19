package com.tsh.starter.befw.app.server.actiavtor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.tsh.starter.befw.lib.core.messaging.solace.SolaceSessionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppStarter implements ApplicationRunner {

	@Autowired
	SolaceSessionManager solaceSessionManager;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		log.info("application started.");
		this.startConnectMessagingServer();

	}

	private void startConnectMessagingServer() {
		this.solaceSessionManager.init();
	}

}
