package com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectBudget;

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
	name = ApTableName.SN_CIRA_PROJECT_BUDGET
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraProjectBudgetModel extends BaseModel {

	public static final String UK01 = "uk_ciraProjectBudget_01";

	@Column(name = "PROJECT_ID", length = 100, nullable = false)
	private String projectId;

	@Column(name = "TOTAL_BUDGET", nullable = false)
	private java.math.BigDecimal totalBudget;

	@Column(name = "BUDGET_CATEGORY", length = 50, nullable = false)
	private String budgetCategory;

	@Column(name = "CURRENCY", length = 3, nullable = false)
	private String currency;

	@Column(name = "FISCAL_YEAR")
	private Short fiscalYear;

}
