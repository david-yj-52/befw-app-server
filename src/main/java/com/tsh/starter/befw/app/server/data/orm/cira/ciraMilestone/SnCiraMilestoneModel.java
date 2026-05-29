package com.tsh.starter.befw.app.server.data.orm.cira.ciraMilestone;

import java.time.LocalDate;

import org.hibernate.envers.Audited;

import com.tsh.starter.befw.app.server.constant.ApTableName;
import com.tsh.starter.befw.lib.core.data.orm.common.model.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
	name = ApTableName.SN_CIRA_MILESTONE
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraMilestoneModel extends BaseModel {

	@Column(name = "PROJECT_ID", length = 100, nullable = false)
	private String projectId;

	@Column(name = "MILESTONE_NM", length = 200, nullable = false)
	private String milestoneNm;

	@Column(name = "DESCR", columnDefinition = "TEXT")
	private String descr;

	@Column(name = "DUE_DT")
	private LocalDate dueDt;

	@Column(name = "STATUS", length = 50, nullable = false)
	private String status;

}
