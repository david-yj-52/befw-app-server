package com.tsh.starter.befw.app.server.data.orm.cira.ciraIssue;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraIssueRepo extends BaseJpaRepository<SnCiraIssueModel, String> {

	List<SnCiraIssueModel> findBySprintIdAndDeletedAtIsNull(String sprintId);

	List<SnCiraIssueModel> findByProjectIdAndDeletedAtIsNull(String projectId);

	List<SnCiraIssueModel> findByAssigneeIdAndDeletedAtIsNull(String assigneeId);

	@Query("SELECT i FROM SnCiraIssueModel i WHERE i.assigneeId = :userId AND i.deletedAt IS NULL AND i.dueDt <= :deadline")
	List<SnCiraIssueModel> findUpcomingDeadlines(@Param("userId") String userId, @Param("deadline") LocalDate deadline);

	@Query("SELECT i FROM SnCiraIssueModel i WHERE i.sprintId IN :sprintIds AND i.deletedAt IS NULL")
	List<SnCiraIssueModel> findBySprintIds(@Param("sprintIds") List<String> sprintIds);

}
