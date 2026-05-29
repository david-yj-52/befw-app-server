package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueTransition;

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
	name = ApTableName.SN_CIRA_ISSUE_TRANSITION
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraIssueTransitionModel extends BaseModel {

	public static final String UK01 = "uk_ciraIssueTransition_01";

	@Column(name = "PROJECT_ID", length = 100)
	private String projectId;

	@Column(name = "FROM_STATUS_ID", length = 100)
	private String fromStatusId;

	@Column(name = "TO_STATUS_ID", length = 100, nullable = false)
	private String toStatusId;

	@Column(name = "ALLOW_YN", length = 1, nullable = false)
	private String allowYn;

	@Column(name = "REQUIRED_ROLE", length = 50)
	private String requiredRole;

}
