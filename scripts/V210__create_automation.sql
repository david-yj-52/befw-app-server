-- ============================================================
-- V210: 자동화 엔진 테이블 생성
-- 위치: {DDL_BASE_PATH}/migration/V210__create_automation.sql
-- ============================================================

-- -----------------------------------------------
-- SN_CIRA_AUTO_RULE: 자동화 규칙 정의 테이블
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS SN_CIRA_AUTO_RULE (
    OBJ_ID          VARCHAR(100)    NOT NULL,
    PROJECT_ID      VARCHAR(100)    NOT NULL,
    RULE_NM         VARCHAR(200)    NOT NULL,
    TRIGGER_TYPE    VARCHAR(100)    NOT NULL,
    TRIGGER_CONFIG  JSONB,
    COND_CONFIG     JSONB,
    ACTION_TYPE     VARCHAR(100)    NOT NULL,
    ACTION_CONFIG   JSONB,
    IS_ACTIVE       BOOLEAN         NOT NULL DEFAULT TRUE,
    SORT_ORD        INTEGER         NOT NULL DEFAULT 0,

    -- BaseModel columns
    USE_STAT_CD     VARCHAR(20),
    SRV_ID          VARCHAR(100),
    TENANT          VARCHAR(100),
    TRACE_ID        VARCHAR(200),
    EVT_NM          VARCHAR(200),
    PREV_EVNT_NM    VARCHAR(200),
    CREATED_AT      TIMESTAMPTZ     DEFAULT NOW(),
    UPDATED_AT      TIMESTAMPTZ     DEFAULT NOW(),
    DELETED_AT      TIMESTAMPTZ,
    CREATED_BY      VARCHAR(100),
    UPDATED_BY      VARCHAR(100),

    CONSTRAINT pk_sn_cira_auto_rule PRIMARY KEY (OBJ_ID),
    CONSTRAINT fk_sn_cira_auto_rule_project FOREIGN KEY (PROJECT_ID) REFERENCES SN_CIRA_PROJECT(OBJ_ID)
);

CREATE INDEX IF NOT EXISTS idx_sn_cira_auto_rule_project ON SN_CIRA_AUTO_RULE (PROJECT_ID);
CREATE INDEX IF NOT EXISTS idx_sn_cira_auto_rule_active  ON SN_CIRA_AUTO_RULE (PROJECT_ID, IS_ACTIVE);

-- -----------------------------------------------
-- SN_CIRA_AUTO_EXECUTION: 자동화 실행 이력 테이블
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS SN_CIRA_AUTO_EXECUTION (
    OBJ_ID          VARCHAR(100)    NOT NULL,
    RULE_ID         VARCHAR(100)    NOT NULL,
    ISSUE_ID        VARCHAR(100),
    EXEC_STAT       VARCHAR(50)     NOT NULL,
    EXECUTED_AT     TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    ERR_MSG         TEXT,

    -- BaseModel columns
    USE_STAT_CD     VARCHAR(20),
    SRV_ID          VARCHAR(100),
    TENANT          VARCHAR(100),
    TRACE_ID        VARCHAR(200),
    EVT_NM          VARCHAR(200),
    PREV_EVNT_NM    VARCHAR(200),
    CREATED_AT      TIMESTAMPTZ     DEFAULT NOW(),
    UPDATED_AT      TIMESTAMPTZ     DEFAULT NOW(),
    DELETED_AT      TIMESTAMPTZ,
    CREATED_BY      VARCHAR(100),
    UPDATED_BY      VARCHAR(100),

    CONSTRAINT pk_sn_cira_auto_execution PRIMARY KEY (OBJ_ID),
    CONSTRAINT fk_sn_cira_auto_exec_rule  FOREIGN KEY (RULE_ID)  REFERENCES SN_CIRA_AUTO_RULE(OBJ_ID),
    CONSTRAINT fk_sn_cira_auto_exec_issue FOREIGN KEY (ISSUE_ID) REFERENCES SN_CIRA_ISSUE(OBJ_ID)
);

CREATE INDEX IF NOT EXISTS idx_sn_cira_auto_exec_rule     ON SN_CIRA_AUTO_EXECUTION (RULE_ID);
CREATE INDEX IF NOT EXISTS idx_sn_cira_auto_exec_issue    ON SN_CIRA_AUTO_EXECUTION (ISSUE_ID);
CREATE INDEX IF NOT EXISTS idx_sn_cira_auto_exec_loop     ON SN_CIRA_AUTO_EXECUTION (RULE_ID, ISSUE_ID, EXECUTED_AT);
