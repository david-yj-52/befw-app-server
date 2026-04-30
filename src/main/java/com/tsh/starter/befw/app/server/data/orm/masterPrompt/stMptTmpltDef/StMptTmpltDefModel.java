package com.tsh.starter.befw.app.server.data.orm.masterPrompt.stMptTmpltDef;

import org.hibernate.annotations.Comment;
import org.hibernate.envers.Audited;

import com.tsh.starter.befw.app.server.constant.ApTableName;
import com.tsh.starter.befw.app.server.data.orm.masterPrompt.constant.TemplateTyp;
import com.tsh.starter.befw.app.server.data.orm.masterPrompt.stMptRoleTmpltRel.StMptWrkrTmpltRelModel;
import com.tsh.starter.befw.lib.core.data.orm.common.model.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
	name = ApTableName.ST_MPT_TMPL_DEF,
	uniqueConstraints = {
		@UniqueConstraint(name = StMptWrkrTmpltRelModel.UK01, columnNames = {"templateId", "templateNm", "templateTyp"})
	}
)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Audited
public class StMptTmpltDefModel extends BaseModel {

	@Comment("template id")
	@Column(name = "TMPL_ID", nullable = false, length = 100)
	private String templateId;

	@Comment("template name")
	@Column(name = "TMPL_NM", nullable = false, length = 100)
	private String templateNm;

	@Enumerated(EnumType.STRING)
	@Comment("template type")
	@Column(name = "TMPL_TYP", nullable = false, length = 100)
	private TemplateTyp templateTyp;

	@Comment("template content")
	@Column(name = "TMPL_CNTNT", nullable = false)
	private TemplateTyp templateContent;

}
