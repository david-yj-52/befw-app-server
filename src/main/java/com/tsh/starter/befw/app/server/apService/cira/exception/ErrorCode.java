package com.tsh.starter.befw.app.server.apService.cira.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ISSUE_INVALID_TRANSITION("CIRA_001", "허용되지 않은 상태 전이입니다."),
    ISSUE_STATUS_NOT_FOUND("CIRA_002", "존재하지 않는 이슈 상태입니다."),
    ISSUE_NOT_FOUND("CIRA_003", "존재하지 않는 이슈입니다."),
    PROJECT_NOT_MEMBER("CIRA_004", "프로젝트 멤버가 아닙니다.");

    private final String code;
    private final String defaultMessage;
}
