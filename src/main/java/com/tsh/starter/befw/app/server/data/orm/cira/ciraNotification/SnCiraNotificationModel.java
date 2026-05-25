package com.tsh.starter.befw.app.server.data.orm.cira.ciraNotification;

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
	name = ApTableName.SN_CIRA_NOTIFICATION
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraNotificationModel extends BaseModel {

	public static final String UK01 = "uk_ciraNotification_01";

	@Column(name = "USER_ID", length = 100, nullable = false)
	private String userId;

	@Column(name = "NOTIF_TYPE", length = 50, nullable = false)
	private String notifType;

	@Column(name = "TITLE", length = 300, nullable = false)
	private String title;

	@Column(name = "MSG")
	private String msg;

	@Column(name = "RESOURCE_TYPE", length = 50)
	private String resourceType;

	@Column(name = "RESOURCE_ID", length = 100)
	private String resourceId;

	@Column(name = "READ_YN", length = 1, nullable = false)
	private String readYn;

}
