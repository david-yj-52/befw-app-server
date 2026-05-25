package com.tsh.starter.befw.app.server.data.orm.cira.ciraSavedFilter;

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
	name = ApTableName.SN_CIRA_SAVED_FILTER
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraSavedFilterModel extends BaseModel {

	public static final String UK01 = "uk_ciraSavedFilter_01";

	@Column(name = "USER_ID", length = 100, nullable = false)
	private String userId;

	@Column(name = "PROJECT_ID", length = 100)
	private String projectId;

	@Column(name = "FILTER_NM", length = 200, nullable = false)
	private String filterNm;

	@Column(name = "JQL_QUERY")
	private String jqlQuery;

	@Column(name = "SHARED_YN", length = 1, nullable = false)
	private String sharedYn;

}
