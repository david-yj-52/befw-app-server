package com.tsh.starter.befw.app.server.data.orm.cira.ciraAutoExecution;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraAutoExecutionRepo extends BaseJpaRepository<SnCiraAutoExecutionModel, String> {
}
