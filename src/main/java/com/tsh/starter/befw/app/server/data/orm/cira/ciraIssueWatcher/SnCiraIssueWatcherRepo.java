package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueWatcher;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraIssueWatcherRepo extends BaseJpaRepository<SnCiraIssueWatcherModel, String> {
}
