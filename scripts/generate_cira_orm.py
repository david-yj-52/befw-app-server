#!/usr/bin/env python3
"""Generate Cira SN_* ORM layer (Model, Repo, Access)."""
from __future__ import annotations

import os
from pathlib import Path

BASE_PKG = "com.tsh.starter.befw.app.server.data.orm.cira"
ROOT = Path(__file__).resolve().parents[1] / "src/main/java/com/tsh/starter/befw/app/server/data/orm/cira"
AP_TABLE = Path(__file__).resolve().parents[1] / "src/main/java/com/tsh/starter/befw/app/server/constant/ApTableName.java"

# (table_name, uk_columns or None)
# field: (COL, javaName, type, length, nullable, extra_column_attrs)
# type: String, Text, BigDecimal, Long, Integer, Short, LocalDate, LocalDateTime, Json

def folder_name(table: str) -> str:
    s = table[3:]  # drop SN_
    parts = s.split("_")
    return parts[0].lower() + "".join(p.capitalize() for p in parts[1:])


def class_base(table: str) -> str:
    s = table[3:]
    parts = s.split("_")
    return "Sn" + "".join(p.capitalize() for p in parts)


def java_type(t: str) -> str:
    return {
        "String": "String",
        "Text": "String",
        "BigDecimal": "java.math.BigDecimal",
        "Long": "Long",
        "Integer": "Integer",
        "Short": "Short",
        "LocalDate": "java.time.LocalDate",
        "LocalDateTime": "java.time.LocalDateTime",
        "Json": "String",
    }[t]


def field_column(f: tuple) -> str:
    col, name, typ, length, nullable, extra = f
    jt = java_type(typ)
    lines = []
    if typ == "Json":
        lines.append(f'\t@Column(name = "{col}", columnDefinition = "jsonb")')
    elif typ == "Text":
        lines.append(f'\t@Column(name = "{col}")')
    else:
        attrs = [f'name = "{col}"']
        if length:
            attrs.append(f"length = {length}")
        if not nullable:
            attrs.append("nullable = false")
        lines.append(f"\t@Column({', '.join(attrs)})")
    lines.append(f"\tprivate {jt} {name};")
    return "\n".join(lines)


ENTITIES: list[tuple[str, list | None, list]] = [
    ("SN_CIRA_PROJECT", ["PROJECT_KEY"], [
        ("PROJECT_KEY", "projectKey", "String", 20, False, None),
        ("PROJECT_NM", "projectNm", "String", 200, False, None),
        ("DESCR", "descr", "Text", None, True, None),
        ("PROJECT_TYPE", "projectType", "String", 30, False, None),
        ("OWNER_ID", "ownerId", "String", 100, True, None),
    ]),
    ("SN_CIRA_PROJECT_MEMBER", ["PROJECT_ID", "USER_ID"], [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("USER_ID", "userId", "String", 100, False, None),
        ("ROLE", "role", "String", 50, False, None),
    ]),
    ("SN_CIRA_SPRINT", None, [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("SPRINT_NM", "sprintNm", "String", 200, False, None),
        ("GOAL", "goal", "Text", None, True, None),
        ("START_DT", "startDt", "LocalDate", None, True, None),
        ("END_DT", "endDt", "LocalDate", None, True, None),
    ]),
    ("SN_CIRA_SPRINT_METRICS", ["SPRINT_ID"], [
        ("SPRINT_ID", "sprintId", "String", 100, False, None),
        ("VELOCITY", "velocity", "BigDecimal", None, True, None),
        ("TEAM_CAPACITY", "teamCapacity", "BigDecimal", None, True, None),
        ("PLAN_STORY_PNT", "planStoryPnt", "BigDecimal", None, True, None),
        ("COMPL_STORY_PNT", "complStoryPnt", "BigDecimal", None, True, None),
    ]),
    ("SN_CIRA_CIRA_ISSUE_TYPE", ["TYPE_NM"], [
        ("TYPE_NM", "typeNm", "String", 50, False, None),
        ("ICON", "icon", "String", 100, True, None),
        ("COLOR_CD", "colorCd", "String", 7, True, None),
        ("DESCR", "descr", "String", 255, True, None),
    ]),
    ("SN_CIRA_ISSUE_STATUS", ["PROJECT_ID", "STATUS_NM"], [
        ("PROJECT_ID", "projectId", "String", 100, True, None),
        ("STATUS_NM", "statusNm", "String", 50, False, None),
        ("CATEGORY", "category", "String", 20, False, None),
        ("COLOR_CD", "colorCd", "String", 7, True, None),
        ("SORT_ORD", "sortOrd", "Short", None, False, None),
    ]),
    ("SN_CIRA_ISSUE", ["ISSUE_KEY"], [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("SPRINT_ID", "sprintId", "String", 100, True, None),
        ("ISSUE_KEY", "issueKey", "String", 30, False, None),
        ("TITLE", "title", "String", 500, False, None),
        ("CONTENT", "content", "Text", None, True, None),
        ("ISSUE_TYPE_ID", "issueTypeId", "String", 100, False, None),
        ("STATUS_ID", "statusId", "String", 100, False, None),
        ("PRIORITY", "priority", "String", 20, False, None),
        ("STORY_PNT", "storyPnt", "BigDecimal", None, True, None),
        ("ASSIGNEE_ID", "assigneeId", "String", 100, True, None),
        ("REPORTER_ID", "reporterId", "String", 100, False, None),
        ("DUE_DT", "dueDt", "LocalDate", None, True, None),
        ("STARTED_AT", "startedAt", "LocalDateTime", None, True, None),
        ("RESOLVED_AT", "resolvedAt", "LocalDateTime", None, True, None),
    ]),
    ("SN_CIRA_ISSUE_TRANSITION", None, [
        ("PROJECT_ID", "projectId", "String", 100, True, None),
        ("FROM_STATUS_ID", "fromStatusId", "String", 100, True, None),
        ("TO_STATUS_ID", "toStatusId", "String", 100, False, None),
        ("ALLOW_YN", "allowYn", "String", 1, False, None),
        ("REQUIRED_ROLE", "requiredRole", "String", 50, True, None),
    ]),
    ("SN_CIRA_ISSUE_LOG", None, [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("FIELD_NM", "fieldNm", "String", 100, False, None),
        ("OLD_VAL", "oldVal", "Text", None, True, None),
        ("NEW_VAL", "newVal", "Text", None, True, None),
        ("CHANGED_BY", "changedBy", "String", 100, False, None),
        ("CHANGED_AT", "changedAt", "LocalDateTime", None, False, None),
    ]),
    ("SN_CIRA_ISSUE_WATCHER", ["ISSUE_ID", "USER_ID"], [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("USER_ID", "userId", "String", 100, False, None),
    ]),
    ("SN_CIRA_ISSUE_LINK", ["SRC_ISSUE_ID", "TGT_ISSUE_ID", "LINK_TYPE"], [
        ("SRC_ISSUE_ID", "srcIssueId", "String", 100, False, None),
        ("TGT_ISSUE_ID", "tgtIssueId", "String", 100, False, None),
        ("LINK_TYPE", "linkType", "String", 50, False, None),
    ]),
    ("SN_CIRA_ISSUE_SUBTASK", ["PARENT_ISSUE_ID", "CHILD_ISSUE_ID"], [
        ("PARENT_ISSUE_ID", "parentIssueId", "String", 100, False, None),
        ("CHILD_ISSUE_ID", "childIssueId", "String", 100, False, None),
        ("SORT_ORD", "sortOrd", "Short", None, False, None),
    ]),
    ("SN_CIRA_COMMENT", None, [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("AUTHOR_ID", "authorId", "String", 100, False, None),
        ("PARENT_ID", "parentId", "String", 100, True, None),
        ("CONTENT", "content", "Text", None, False, None),
    ]),
    ("SN_CIRA_COMMENT_REACTION", ["COMMENT_ID", "USER_ID", "REACTION_TYPE"], [
        ("COMMENT_ID", "commentId", "String", 100, False, None),
        ("USER_ID", "userId", "String", 100, False, None),
        ("REACTION_TYPE", "reactionType", "String", 30, False, None),
    ]),
    ("SN_CIRA_ATTACHMENT", None, [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("COMMENT_ID", "commentId", "String", 100, True, None),
        ("FILE_NM", "fileNm", "String", 255, False, None),
        ("FILE_PATH", "filePath", "String", 1000, False, None),
        ("FILE_SIZE", "fileSize", "Long", None, True, None),
        ("MIME_TYPE", "mimeType", "String", 100, True, None),
    ]),
    ("SN_CIRA_BOARD", None, [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("BOARD_NM", "boardNm", "String", 200, False, None),
        ("BOARD_TYPE", "boardType", "String", 20, False, None),
    ]),
    ("SN_CIRA_BOARD_COLUMN", ["BOARD_ID", "SORT_ORD"], [
        ("BOARD_ID", "boardId", "String", 100, False, None),
        ("STATUS_ID", "statusId", "String", 100, False, None),
        ("COLUMN_NM", "columnNm", "String", 100, False, None),
        ("WIP_LIMIT", "wipLimit", "Short", None, True, None),
        ("SORT_ORD", "sortOrd", "Short", None, False, None),
    ]),
    ("SN_CIRA_ISSUE_POSITION", ["ISSUE_ID", "COLUMN_ID"], [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("COLUMN_ID", "columnId", "String", 100, False, None),
        ("RANK_STR", "rankStr", "String", 100, False, None),
    ]),
    ("SN_CIRA_CUSTOM_FIELD", ["PROJECT_ID", "FIELD_NM"], [
        ("PROJECT_ID", "projectId", "String", 100, True, None),
        ("FIELD_NM", "fieldNm", "String", 100, False, None),
        ("FIELD_TYPE", "fieldType", "String", 30, False, None),
        ("REQUIRED_YN", "requiredYn", "String", 1, False, None),
        ("OPTIONS", "options", "Json", None, True, None),
        ("SORT_ORD", "sortOrd", "Short", None, False, None),
    ]),
    ("SN_CIRA_ISSUE_CF_VALUE", ["ISSUE_ID", "CUSTOM_FIELD_ID"], [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("CUSTOM_FIELD_ID", "customFieldId", "String", 100, False, None),
        ("VAL_TEXT", "valText", "Text", None, True, None),
        ("VAL_NUMBER", "valNumber", "BigDecimal", None, True, None),
        ("VAL_DT", "valDt", "LocalDate", None, True, None),
        ("VAL_JSON", "valJson", "Json", None, True, None),
    ]),
    ("SN_CIRA_PROJECT_BUDGET", None, [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("TOTAL_BUDGET", "totalBudget", "BigDecimal", None, False, None),
        ("BUDGET_CATEGORY", "budgetCategory", "String", 50, False, None),
        ("CURRENCY", "currency", "String", 3, False, None),
        ("FISCAL_YEAR", "fiscalYear", "Short", None, True, None),
    ]),
    ("SN_CIRA_HOURLY_RATE", None, [
        ("USER_ID", "userId", "String", 100, False, None),
        ("HOURLY_RATE", "hourlyRate", "BigDecimal", None, False, None),
        ("CURRENCY", "currency", "String", 3, False, None),
        ("EFF_FROM_DT", "effFromDt", "LocalDate", None, False, None),
        ("EFF_TO_DT", "effToDt", "LocalDate", None, True, None),
    ]),
    ("SN_CIRA_TIME_LOG", None, [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("USER_ID", "userId", "String", 100, False, None),
        ("LOG_HRS", "logHrs", "BigDecimal", None, False, None),
        ("LOG_DT", "logDt", "LocalDate", None, False, None),
        ("DESCR", "descr", "Text", None, True, None),
    ]),
    ("SN_CIRA_EXPENSE", None, [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("ISSUE_ID", "issueId", "String", 100, True, None),
        ("AMOUNT", "amount", "BigDecimal", None, False, None),
        ("CURRENCY", "currency", "String", 3, False, None),
        ("CATEGORY", "category", "String", 50, False, None),
        ("EXPENSE_DT", "expenseDt", "LocalDate", None, False, None),
        ("DESCR", "descr", "Text", None, True, None),
    ]),
    ("SN_CIRA_AUTO_RULE", None, [
        ("PROJECT_ID", "projectId", "String", 100, True, None),
        ("RULE_NM", "ruleNm", "String", 200, False, None),
        ("DESCR", "descr", "Text", None, True, None),
        ("TRIGGER_TYPE", "triggerType", "String", 50, False, None),
        ("COND", "cond", "Json", None, True, None),
        ("ACTION", "action", "Json", None, False, None),
    ]),
    ("SN_CIRA_AUTO_EXECUTION", None, [
        ("RULE_ID", "ruleId", "String", 100, False, None),
        ("ISSUE_ID", "issueId", "String", 100, True, None),
        ("EXEC_STAT", "execStat", "String", 20, False, None),
        ("ERR_MSG", "errMsg", "Text", None, True, None),
        ("EXECUTED_AT", "executedAt", "LocalDateTime", None, False, None),
    ]),
    ("SN_CIRA_NOTIFICATION", None, [
        ("USER_ID", "userId", "String", 100, False, None),
        ("NOTIF_TYPE", "notifType", "String", 50, False, None),
        ("TITLE", "title", "String", 300, False, None),
        ("MSG", "msg", "Text", None, True, None),
        ("RESOURCE_TYPE", "resourceType", "String", 50, True, None),
        ("RESOURCE_ID", "resourceId", "String", 100, True, None),
        ("READ_YN", "readYn", "String", 1, False, None),
    ]),
    ("SN_CIRA_NOTIF_PREF", ["USER_ID", "CHANNEL", "EVENT_TYPE"], [
        ("USER_ID", "userId", "String", 100, False, None),
        ("CHANNEL", "channel", "String", 30, False, None),
        ("EVENT_TYPE", "eventType", "String", 50, False, None),
        ("ENABLED_YN", "enabledYn", "String", 1, False, None),
    ]),
    ("SN_CIRA_AUDIT_LOG", None, [
        ("ACTION_TYPE", "actionType", "String", 30, False, None),
        ("ACTOR_ID", "actorId", "String", 100, True, None),
        ("RESOURCE_TYPE", "resourceType", "String", 50, False, None),
        ("RESOURCE_ID", "resourceId", "String", 100, False, None),
        ("OLD_SNAPSHOT", "oldSnapshot", "Json", None, True, None),
        ("NEW_SNAPSHOT", "newSnapshot", "Json", None, True, None),
        ("IP_ADDR", "ipAddr", "String", 45, True, None),
        ("USER_AGENT", "userAgent", "Text", None, True, None),
    ]),
    ("SN_CIRA_GIT_REPO", ["PROJECT_ID", "REPO_URL"], [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("PROVIDER", "provider", "String", 20, False, None),
        ("REPO_URL", "repoUrl", "String", 500, False, None),
        ("ACCESS_TOKEN_ENC", "accessTokenEnc", "Text", None, True, None),
        ("DEFAULT_BRANCH", "defaultBranch", "String", 100, False, None),
    ]),
    ("SN_CIRA_GIT_COMMIT", ["REPO_ID", "COMMIT_HASH"], [
        ("REPO_ID", "repoId", "String", 100, False, None),
        ("ISSUE_ID", "issueId", "String", 100, True, None),
        ("COMMIT_HASH", "commitHash", "String", 40, False, None),
        ("MSG", "msg", "Text", None, False, None),
        ("AUTHOR_NM", "authorNm", "String", 100, True, None),
        ("AUTHOR_EMAIL", "authorEmail", "String", 255, True, None),
        ("COMMIT_DT", "commitDt", "LocalDateTime", None, False, None),
    ]),
    ("SN_CIRA_GIT_PR", ["REPO_ID", "PR_NO"], [
        ("REPO_ID", "repoId", "String", 100, False, None),
        ("ISSUE_ID", "issueId", "String", 100, True, None),
        ("PR_NO", "prNo", "Integer", None, False, None),
        ("TITLE", "title", "String", 500, False, None),
        ("DESCR", "descr", "Text", None, True, None),
        ("PR_STAT", "prStat", "String", 20, False, None),
        ("SRC_BRANCH", "srcBranch", "String", 200, True, None),
        ("TGT_BRANCH", "tgtBranch", "String", 200, True, None),
        ("AUTHOR_NM", "authorNm", "String", 100, True, None),
        ("AUTHOR_EMAIL", "authorEmail", "String", 255, True, None),
        ("MERGED_AT", "mergedAt", "LocalDateTime", None, True, None),
        ("CLOSED_AT", "closedAt", "LocalDateTime", None, True, None),
    ]),
    ("SN_CIRA_VERSION", ["PROJECT_ID", "VERSION_NM"], [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("VERSION_NM", "versionNm", "String", 100, False, None),
        ("DESCR", "descr", "Text", None, True, None),
        ("PLAN_REL_DT", "planRelDt", "LocalDate", None, True, None),
        ("RELEASED_DT", "releasedDt", "LocalDate", None, True, None),
    ]),
    ("SN_CIRA_ISSUE_VERSION", ["ISSUE_ID", "VERSION_ID", "REL_TYPE"], [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("VERSION_ID", "versionId", "String", 100, False, None),
        ("REL_TYPE", "relType", "String", 30, False, None),
    ]),
    ("SN_CIRA_MILESTONE", None, [
        ("PROJECT_ID", "projectId", "String", 100, False, None),
        ("MILESTONE_NM", "milestoneNm", "String", 200, False, None),
        ("DESCR", "descr", "Text", None, True, None),
        ("DUE_DT", "dueDt", "LocalDate", None, True, None),
    ]),
    ("SN_CIRA_MILESTONE_ISSUE", ["MILESTONE_ID", "ISSUE_ID"], [
        ("MILESTONE_ID", "milestoneId", "String", 100, False, None),
        ("ISSUE_ID", "issueId", "String", 100, False, None),
    ]),
    ("SN_CIRA_SAVED_FILTER", None, [
        ("USER_ID", "userId", "String", 100, False, None),
        ("PROJECT_ID", "projectId", "String", 100, True, None),
        ("FILTER_NM", "filterNm", "String", 200, False, None),
        ("JQL_QUERY", "jqlQuery", "Text", None, False, None),
        ("SHARED_YN", "sharedYn", "String", 1, False, None),
    ]),
    ("SN_CIRA_ISSUE_SEARCH_IDX", ["ISSUE_ID"], [
        ("ISSUE_ID", "issueId", "String", 100, False, None),
        ("SEARCH_VEC", "searchVec", "String", None, True, None),
    ]),
]


def model_java(table: str, uk: list | None, fields: list) -> str:
    folder = folder_name(table)
    base = class_base(table)
    pkg = f"{BASE_PKG}.{folder}"
    uk_name = f"uk_{folder}_01"
    uk_block = ""
    if uk:
        cols = ", ".join(f'"{c}"' for c in uk)
        uk_block = f""",
\tuniqueConstraints = {{
\t\t@UniqueConstraint(name = {base}Model.UK01, columnNames = {{{cols}}})
\t}}"""

    field_lines = "\n\n".join(field_column(f) for f in fields)
    return f"""package {pkg};

import org.hibernate.envers.Audited;

import com.tsh.starter.befw.app.server.constant.ApTableName;
import com.tsh.starter.befw.lib.core.data.orm.common.model.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
\tname = ApTableName.{table}{uk_block}
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class {base}Model extends BaseModel {{

\tpublic static final String UK01 = "{uk_name}";

{field_lines}

}}
"""


def repo_java(table: str) -> str:
    folder = folder_name(table)
    base = class_base(table)
    pkg = f"{BASE_PKG}.{folder}"
    return f"""package {pkg};

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface {base}Repo extends BaseJpaRepository<{base}Model, String> {{
}}
"""


def access_java(table: str) -> str:
    folder = folder_name(table)
    base = class_base(table)
    pkg = f"{BASE_PKG}.{folder}"
    return f"""package {pkg};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.lib.core.data.orm.common.access.AbstractCrudService;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class {base}Access extends AbstractCrudService<{base}Model, String> {{

\t@Autowired
\t{base}Repo repo;

\t@Override
	protected BaseJpaRepository<{base}Model, String> getRepository() {{
\t\treturn repo;
\t}}

}}
"""


def main() -> None:
    ROOT.mkdir(parents=True, exist_ok=True)
    (ROOT / "package-info.java").write_text(
        "/** Cira application ORM (SN_CIRA_* tables). */\n"
        f"package {BASE_PKG};\n",
        encoding="utf-8",
    )

    tables = []
    for table, uk, fields in ENTITIES:
        folder = folder_name(table)
        d = ROOT / folder
        d.mkdir(parents=True, exist_ok=True)
        base = class_base(table)
        (d / f"{base}Model.java").write_text(model_java(table, uk, fields), encoding="utf-8")
        (d / f"{base}Repo.java").write_text(repo_java(table), encoding="utf-8")
        (d / f"{base}Access.java").write_text(access_java(table), encoding="utf-8")
        tables.append(table)
        print(f"Generated {folder}/")

    st_lines = [
        '\tpublic static final String ST_MPT_ROLE_TMPLT_REL = "ST_MPT_ROLE_TMPLT_REL";',
        '\tpublic static final String ST_MPT_TMPL_DEF = "ST_MPT_TMPL_DEF";',
        '\tpublic static final String ST_ORG_UNIT_DEF = "ST_ORG_UNIT_DEF";',
        '\tpublic static final String ST_ORG_WRKR_REL = "ST_ORG_WRKR_REL";',
        "",
    ]
    sn_lines = [f'\tpublic static final String {t} = "{t}";' for t in tables]
    ap_content = """package com.tsh.starter.befw.app.server.constant;

/**
 * Application table names: ST (server), SN_CIRA (Cira app).
 */
public class ApTableName {

""" + "\n".join(st_lines) + "\n".join(sn_lines) + "\n}\n"
    Path(AP_TABLE).write_text(ap_content, encoding="utf-8")
    print("Updated ApTableName.java")


if __name__ == "__main__":
    main()
