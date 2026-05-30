package com.tsh.starter.befw.app.server.data.orm.cira.ciraSprint;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.constant.UseStatCd;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraSprintRepo extends BaseJpaRepository<SnCiraSprintModel, String> {

	List<SnCiraSprintModel> findByProjectId(String projectId);

	List<SnCiraSprintModel> findByProjectIdAndSprintStat(String projectId, String sprintStat);

	List<SnCiraSprintModel> findBySprintStatAndUseStatCd(String sprintStat, UseStatCd useStatCd);

}
