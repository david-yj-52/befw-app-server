package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueTransition;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraIssueTransitionRepo extends BaseJpaRepository<SnCiraIssueTransitionModel, String> {

    List<SnCiraIssueTransitionModel> findByProjectId(String projectId);

    List<SnCiraIssueTransitionModel> findByProjectIdAndFromStatusId(String projectId, String fromStatusId);
}
