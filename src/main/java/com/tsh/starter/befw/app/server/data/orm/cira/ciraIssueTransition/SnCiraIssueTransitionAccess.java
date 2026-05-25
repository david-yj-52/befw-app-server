package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueTransition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.lib.core.data.orm.common.access.AbstractCrudService;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnCiraIssueTransitionAccess extends AbstractCrudService<SnCiraIssueTransitionModel, String> {

	@Autowired
	SnCiraIssueTransitionRepo repo;

	@Override
	protected BaseJpaRepository<SnCiraIssueTransitionModel, String> getRepository() {
		return repo;
	}

}
