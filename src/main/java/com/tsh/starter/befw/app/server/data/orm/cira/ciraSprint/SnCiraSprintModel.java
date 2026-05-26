package com.tsh.starter.befw.app.server.data.orm.cira.ciraSprint;

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
	name = ApTableName.SN_CIRA_SPRINT
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraSprintModel extends BaseModel {

	public static final String UK01 = "uk_ciraSprint_01";

	@Column(name = "PROJECT_ID", length = 100, nullable = false)
	private String projectId;

	@Column(name = "SPRINT_NM", length = 200, nullable = false)
	private String sprintNm;

	@Column(name = "GOAL")
	private String goal;

	@Column(name = "START_DT")
	private java.time.LocalDate startDt;

	@Column(name = "END_DT")
	private java.time.LocalDate endDt;

	@Column(name = "SPRINT_STAT", length = 20, nullable = false)
	private String sprintStat;

}
