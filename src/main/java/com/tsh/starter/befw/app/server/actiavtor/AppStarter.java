package com.tsh.starter.befw.app.server.actiavtor;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppStarter implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {

		log.info("application started.");

	}

}
