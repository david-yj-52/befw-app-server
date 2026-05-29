package com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectBudget;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraProjectBudgetRepo extends BaseJpaRepository<SnCiraProjectBudgetModel, String> {
}
