package com.tsh.starter.befw.app.server.apService.cira.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    ISSUE_INVALID_TRANSITION("CIRA_001", "허용되지 않은 상태 전이입니다."),
    ISSUE_STATUS_NOT_FOUND("CIRA_002", "존재하지 않는 이슈 상태입니다."),
    ISSUE_NOT_FOUND("CIRA_003", "존재하지 않는 이슈입니다."),
    PROJECT_NOT_MEMBER("CIRA_004", "프로젝트 멤버가 아닙니다."),
    BOARD_NOT_FOUND("CIRA_005", "존재하지 않는 보드입니다."),
    BOARD_COLUMN_NOT_FOUND("CIRA_006", "존재하지 않는 보드 컬럼입니다."),
    COMMENT_NOT_FOUND("CIRA_007", "존재하지 않는 댓글입니다."),
    COMMENT_NOT_AUTHOR("CIRA_008", "댓글 작성자만 수정/삭제할 수 있습니다."),
    SPRINT_NOT_FOUND("CIRA_009", "존재하지 않는 스프린트입니다."),
    SPRINT_INVALID_TRANSITION("CIRA_010", "허용되지 않은 스프린트 상태 전이입니다."),
    SPRINT_ALREADY_ACTIVE("CIRA_011", "이미 활성화된 스프린트가 존재합니다."),
    BOARD_WIP_LIMIT_EXCEEDED("CIRA_012", "보드 컬럼의 WIP 제한을 초과하였습니다.");

    private final String code;
    private final String defaultMessage;
}
