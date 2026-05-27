package com.tsh.starter.befw.app.server.data.orm.cira.ciraAttachment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.lib.core.data.constant.UseStatCd;
import com.tsh.starter.befw.lib.core.data.orm.common.access.AbstractCrudService;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnCiraAttachmentAccess extends AbstractCrudService<SnCiraAttachmentModel, String> {

	@Autowired
	SnCiraAttachmentRepo repo;

	@Override
	protected BaseJpaRepository<SnCiraAttachmentModel, String> getRepository() {
		return repo;
	}

	public List<SnCiraAttachmentModel> findActiveByIssueId(String issueId) {
		return repo.findByIssueIdAndUseStatCdNot(issueId, UseStatCd.Delete);
	}

}
