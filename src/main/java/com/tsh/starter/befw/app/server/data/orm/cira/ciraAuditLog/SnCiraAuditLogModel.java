package com.tsh.starter.befw.app.server.data.orm.cira.ciraAuditLog;

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
	name = ApTableName.SN_CIRA_AUDIT_LOG
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraAuditLogModel extends BaseModel {

	public static final String UK01 = "uk_ciraAuditLog_01";

	@Column(name = "ACTION_TYPE", length = 30, nullable = false)
	private String actionType;

	@Column(name = "ACTOR_ID", length = 100)
	private String actorId;

	@Column(name = "RESOURCE_TYPE", length = 50, nullable = false)
	private String resourceType;

	@Column(name = "RESOURCE_ID", length = 100, nullable = false)
	private String resourceId;

	@Column(name = "OLD_SNAPSHOT", columnDefinition = "jsonb")
	private String oldSnapshot;

	@Column(name = "NEW_SNAPSHOT", columnDefinition = "jsonb")
	private String newSnapshot;

	@Column(name = "IP_ADDR", length = 45)
	private String ipAddr;

	@Column(name = "USER_AGENT")
	private String userAgent;

}
