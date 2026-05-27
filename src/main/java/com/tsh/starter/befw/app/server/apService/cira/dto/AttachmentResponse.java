package com.tsh.starter.befw.app.server.apService.cira.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttachmentResponse {

	private String id;
	private String issueId;
	private String fileName;
	private Long fileSize;
	private String mimeType;
	private String uploadedBy;
	private LocalDateTime uploadedAt;
	private String downloadUrl;

}
