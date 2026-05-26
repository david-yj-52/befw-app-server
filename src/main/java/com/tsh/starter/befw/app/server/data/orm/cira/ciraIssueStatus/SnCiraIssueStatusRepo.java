package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraIssueStatusRepo extends BaseJpaRepository<SnCiraIssueStatusModel, String> {

    List<SnCiraIssueStatusModel> findByProjectIdOrderBySortOrd(String projectId);

    Optional<SnCiraIssueStatusModel> findByProjectIdAndStatusNm(String projectId, String statusNm);
}
