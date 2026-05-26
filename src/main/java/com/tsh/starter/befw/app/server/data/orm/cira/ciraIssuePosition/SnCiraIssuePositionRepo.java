package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssuePosition;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraIssuePositionRepo extends BaseJpaRepository<SnCiraIssuePositionModel, String> {

    List<SnCiraIssuePositionModel> findByIssueId(String issueId);
}
