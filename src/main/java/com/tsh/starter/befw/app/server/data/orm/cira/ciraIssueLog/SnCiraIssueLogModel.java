package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueLog;

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
	name = ApTableName.SN_CIRA_ISSUE_LOG
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraIssueLogModel extends BaseModel {

	public static final String UK01 = "uk_ciraIssueLog_01";

	@Column(name = "ISSUE_ID", length = 100, nullable = false)
	private String issueId;

	@Column(name = "FIELD_NM", length = 100, nullable = false)
	private String fieldNm;

	@Column(name = "OLD_VAL")
	private String oldVal;

	@Column(name = "NEW_VAL")
	private String newVal;

	@Column(name = "CHANGED_BY", length = 100, nullable = false)
	private String changedBy;

	@Column(name = "CHANGED_AT", nullable = false)
	private java.time.LocalDateTime changedAt;

}
