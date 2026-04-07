package com.tsh.starter.befw.app.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
	"com.tsh.starter.befw.lib.core"
})
@EnableJpaAuditing
class BefwAppServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BefwAppServerApplication.class, args);
	}

}
