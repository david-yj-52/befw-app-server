package com.tsh.starter.befw.app.server.data.orm.cira.ciraAttachment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.constant.UseStatCd;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraAttachmentRepo extends BaseJpaRepository<SnCiraAttachmentModel, String> {

	List<SnCiraAttachmentModel> findByIssueIdAndUseStatCdNot(String issueId, UseStatCd useStatCd);

	long countByIssueIdAndUseStatCdNot(String issueId, UseStatCd useStatCd);

}
