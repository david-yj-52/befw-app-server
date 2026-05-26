package com.tsh.starter.befw.app.server.data.orm.cira.ciraSprint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.lib.core.data.orm.common.access.AbstractCrudService;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnCiraSprintAccess extends AbstractCrudService<SnCiraSprintModel, String> {

	@Autowired
	SnCiraSprintRepo repo;

	@Override
	protected BaseJpaRepository<SnCiraSprintModel, String> getRepository() {
		return repo;
	}

	public List<SnCiraSprintModel> findByProjectId(String projectId) {
		return repo.findByProjectId(projectId);
	}

	public List<SnCiraSprintModel> findByProjectIdAndSprintStat(String projectId, String sprintStat) {
		return repo.findByProjectIdAndSprintStat(projectId, sprintStat);
	}

}
