package com.tsh.starter.befw.app.server.data.orm.cira.ciraProject;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.app.server.data.orm.cira.ciraProject.SnCiraProjectModel;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraProjectRepo extends BaseJpaRepository<SnCiraProjectModel, String> {

	Optional<SnCiraProjectModel> findByProjectKey(String projectKey);

}
