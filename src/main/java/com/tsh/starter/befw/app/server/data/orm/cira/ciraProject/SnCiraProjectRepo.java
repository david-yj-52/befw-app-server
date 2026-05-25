package com.tsh.starter.befw.app.server.data.orm.cira.ciraProject;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraProjectRepo extends BaseJpaRepository<SnCiraProjectModel, String> {
}
