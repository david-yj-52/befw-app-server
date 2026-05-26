package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssue;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraIssueRepo extends BaseJpaRepository<SnCiraIssueModel, String> {

	List<SnCiraIssueModel> findBySprintIdAndDeletedAtIsNull(String sprintId);

}
