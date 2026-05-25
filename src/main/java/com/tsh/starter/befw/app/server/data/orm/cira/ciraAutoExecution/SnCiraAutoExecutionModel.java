package com.tsh.starter.befw.app.server.data.orm.cira.ciraAutoExecution;

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
	name = ApTableName.SN_CIRA_AUTO_EXECUTION
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraAutoExecutionModel extends BaseModel {

	public static final String UK01 = "uk_ciraAutoExecution_01";

	@Column(name = "RULE_ID", length = 100, nullable = false)
	private String ruleId;

	@Column(name = "ISSUE_ID", length = 100)
	private String issueId;

	@Column(name = "EXEC_STAT", length = 20, nullable = false)
	private String execStat;

	@Column(name = "ERR_MSG")
	private String errMsg;

	@Column(name = "EXECUTED_AT", nullable = false)
	private java.time.LocalDateTime executedAt;

}
