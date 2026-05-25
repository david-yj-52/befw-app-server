package com.tsh.starter.befw.app.server.data.orm.cira.ciraExpense;

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
	name = ApTableName.SN_CIRA_EXPENSE
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraExpenseModel extends BaseModel {

	public static final String UK01 = "uk_ciraExpense_01";

	@Column(name = "PROJECT_ID", length = 100, nullable = false)
	private String projectId;

	@Column(name = "ISSUE_ID", length = 100)
	private String issueId;

	@Column(name = "AMOUNT", nullable = false)
	private java.math.BigDecimal amount;

	@Column(name = "CURRENCY", length = 3, nullable = false)
	private String currency;

	@Column(name = "CATEGORY", length = 50, nullable = false)
	private String category;

	@Column(name = "EXPENSE_DT", nullable = false)
	private java.time.LocalDate expenseDt;

	@Column(name = "DESCR")
	private String descr;

}
