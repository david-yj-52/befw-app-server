package com.tsh.starter.befw.app.server.data.orm.cira.ciraMilestone;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraMilestoneRepo extends BaseJpaRepository<SnCiraMilestoneModel, String> {
}
