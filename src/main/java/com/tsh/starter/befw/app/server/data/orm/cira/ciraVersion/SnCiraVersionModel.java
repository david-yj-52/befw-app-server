package com.tsh.starter.befw.app.server.data.orm.cira.ciraVersion;

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
	name = ApTableName.SN_CIRA_VERSION,
	uniqueConstraints = {
		@UniqueConstraint(name = SnCiraVersionModel.UK01, columnNames = {"PROJECT_ID", "VERSION_NM"})
	}
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraVersionModel extends BaseModel {

	public static final String UK01 = "uk_ciraVersion_01";

	@Column(name = "PROJECT_ID", length = 100, nullable = false)
	private String projectId;

	@Column(name = "VERSION_NM", length = 100, nullable = false)
	private String versionNm;

	@Column(name = "DESCR")
	private String descr;

	@Column(name = "PLAN_REL_DT")
	private java.time.LocalDate planRelDt;

	@Column(name = "RELEASED_DT")
	private java.time.LocalDate releasedDt;

}
