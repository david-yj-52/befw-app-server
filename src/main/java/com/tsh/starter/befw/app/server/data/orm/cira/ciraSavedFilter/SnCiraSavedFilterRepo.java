package com.tsh.starter.befw.app.server.data.orm.cira.ciraSavedFilter;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraSavedFilterRepo extends BaseJpaRepository<SnCiraSavedFilterModel, String> {
}
