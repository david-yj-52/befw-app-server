package com.tsh.starter.befw.app.server.data.orm.cira.ciraComment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.lib.core.data.orm.common.access.AbstractCrudService;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnCiraCommentAccess extends AbstractCrudService<SnCiraCommentModel, String> {

	@Autowired
	SnCiraCommentRepo repo;

	@Override
	protected BaseJpaRepository<SnCiraCommentModel, String> getRepository() {
		return repo;
	}

	public List<SnCiraCommentModel> findByIssueId(String issueId) {
		return repo.findByIssueIdOrderByCreatedAtAsc(issueId);
	}

	public List<SnCiraCommentModel> findByParentId(String parentId) {
		return repo.findByParentIdOrderByCreatedAtAsc(parentId);
	}
}
