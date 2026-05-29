package com.tsh.starter.befw.app.server.data.orm.cira.ciraTimeLog;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraTimeLogRepo extends BaseJpaRepository<SnCiraTimeLogModel, String> {
}
