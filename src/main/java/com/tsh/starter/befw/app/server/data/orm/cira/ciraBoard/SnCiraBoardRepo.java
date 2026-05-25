package com.tsh.starter.befw.app.server.data.orm.cira.ciraBoard;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraBoardRepo extends BaseJpaRepository<SnCiraBoardModel, String> {
}
