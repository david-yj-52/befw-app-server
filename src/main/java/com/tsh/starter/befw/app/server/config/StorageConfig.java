package com.tsh.starter.befw.app.server.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
@RequiredArgsConstructor
public class StorageConfig {

	private final StorageProperties props;

	@Bean
	public MinioClient minioClient() {
		return MinioClient.builder()
			.endpoint(props.getEndpoint())
			.credentials(props.getAccessKey(), props.getSecretKey())
			.build();
	}

}
