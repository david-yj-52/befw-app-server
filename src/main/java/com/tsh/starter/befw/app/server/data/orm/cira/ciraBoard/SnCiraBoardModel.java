package com.tsh.starter.befw.app.server.data.orm.cira.ciraBoard;

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
	name = ApTableName.SN_CIRA_BOARD
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class SnCiraBoardModel extends BaseModel {

	public static final String UK01 = "uk_ciraBoard_01";

	@Column(name = "PROJECT_ID", length = 100, nullable = false)
	private String projectId;

	@Column(name = "BOARD_NM", length = 200, nullable = false)
	private String boardNm;

	@Column(name = "BOARD_TYPE", length = 20, nullable = false)
	private String boardType;

}
