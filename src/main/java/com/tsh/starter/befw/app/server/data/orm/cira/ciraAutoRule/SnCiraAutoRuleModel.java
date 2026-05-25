package com.tsh.starter.befw.app.server.data.orm.cira.ciraAutoRule;

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
	name = ApTableName.SN_CIRA_AUTO_RULE
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraAutoRuleModel extends BaseModel {

	public static final String UK01 = "uk_ciraAutoRule_01";

	@Column(name = "PROJECT_ID", length = 100)
	private String projectId;

	@Column(name = "RULE_NM", length = 200, nullable = false)
	private String ruleNm;

	@Column(name = "DESCR")
	private String descr;

	@Column(name = "TRIGGER_TYPE", length = 50, nullable = false)
	private String triggerType;

	@Column(name = "COND", columnDefinition = "jsonb")
	private String cond;

	@Column(name = "ACTION", columnDefinition = "jsonb")
	private String action;

}
