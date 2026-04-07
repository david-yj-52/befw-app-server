package com.tsh.starter.befw.app.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
	"com.tsh.starter.befw.lib.core"
})
@EntityScan(basePackages = {"com.tsh.starter.befw.lib.core"})
@EnableJpaRepositories(basePackages = "com.tsh.starter.befw.lib.core")
class BefwAppServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BefwAppServerApplication.class, args);
	}

}
