package com.tsh.starter.befw.app.server.data.orm.cira.ciraHourlyRate;

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
	name = ApTableName.SN_CIRA_HOURLY_RATE
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraHourlyRateModel extends BaseModel {

	public static final String UK01 = "uk_ciraHourlyRate_01";

	@Column(name = "USER_ID", length = 100, nullable = false)
	private String userId;

	@Column(name = "HOURLY_RATE", nullable = false)
	private java.math.BigDecimal hourlyRate;

	@Column(name = "CURRENCY", length = 3, nullable = false)
	private String currency;

	@Column(name = "EFF_FROM_DT", nullable = false)
	private java.time.LocalDate effFromDt;

	@Column(name = "EFF_TO_DT")
	private java.time.LocalDate effToDt;

}
