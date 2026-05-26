package com.tsh.starter.befw.app.server.apService.cira.dto;

import com.tsh.starter.befw.lib.core.apService.auth.dto.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardIssueResponse {
	private String id;
	private String issueKey;
	private String title;
	private String priority;
	private String issueTypeId;
	private String issueTypeNm;
	private String statusId;
	private String statusNm;
	private UserResponse assignee;
	private String rankStr;
}
