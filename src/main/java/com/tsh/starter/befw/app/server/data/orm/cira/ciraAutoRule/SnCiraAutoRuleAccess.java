package com.tsh.starter.befw.app.server.data.orm.cira.ciraAutoRule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.lib.core.data.orm.common.access.AbstractCrudService;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnCiraAutoRuleAccess extends AbstractCrudService<SnCiraAutoRuleModel, String> {

	@Autowired
	SnCiraAutoRuleRepo repo;

	@Override
	protected BaseJpaRepository<SnCiraAutoRuleModel, String> getRepository() {
		return repo;
	}

}
