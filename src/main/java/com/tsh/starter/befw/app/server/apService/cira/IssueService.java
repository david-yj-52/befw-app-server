package com.tsh.starter.befw.app.server.apService.cira;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsh.starter.befw.app.server.apService.cira.dto.CreateIssueRequest;
import com.tsh.starter.befw.app.server.apService.cira.dto.IssueFilterRequest;
import com.tsh.starter.befw.app.server.apService.cira.dto.IssueResponse;
import com.tsh.starter.befw.app.server.apService.cira.dto.UpdateIssueRequest;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraCiraIssueType.SnCiraCiraIssueTypeAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraCiraIssueType.SnCiraCiraIssueTypeModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssue.SnCiraIssueAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssue.SnCiraIssueModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueLog.SnCiraIssueLogAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueLog.SnCiraIssueLogModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssuePosition.SnCiraIssuePositionAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssuePosition.SnCiraIssuePositionModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueStatus.SnCiraIssueStatusAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueStatus.SnCiraIssueStatusModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProject.SnCiraProjectAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProject.SnCiraProjectModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember.SnCiraProjectMemberAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember.SnCiraProjectMemberModel;
import com.tsh.starter.befw.lib.core.apService.auth.dto.UserResponse;
import com.tsh.starter.befw.lib.core.config.ApplicationProperties;
import com.tsh.starter.befw.lib.core.data.constant.UseStatCd;
import com.tsh.starter.befw.lib.core.data.orm.usersAndPermissions.gsUser.GsUserAccess;
import com.tsh.starter.befw.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {

	private final SnCiraIssueAccess issueAccess;
	private final SnCiraProjectAccess projectAccess;
	private final GsUserAccess userAccess;
	private final SnCiraCiraIssueTypeAccess issueTypeAccess;
	private final SnCiraIssueStatusAccess statusAccess;
	private final SnCiraIssueLogAccess issueLogAccess;
	private final SnCiraIssuePositionAccess issuePositionAccess;
	private final SnCiraProjectMemberAccess projectMemberAccess;

	@Transactional
	public IssueResponse createIssue(String projectId, CreateIssueRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel reporter = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		SnCiraProjectModel project = projectAccess.findById(projectId);

		// Check membership
		projectMemberAccess.findAllByUserId(reporter.getObjId()).stream()
			.filter(m -> m.getProjectId().equals(projectId))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("User is not a member of the project"));

		// Increment sequence and generate key
		project.setIssueSequence(project.getIssueSequence() + 1);
		projectAccess.save(project);
		String issueKey = project.getProjectKey() + "-" + project.getIssueSequence();

		SnCiraIssueModel issue = SnCiraIssueModel.builder()
			.projectId(projectId)
			.sprintId(request.getSprintId())
			.issueKey(issueKey)
			.title(request.getTitle())
			.content(request.getContent())
			.issueTypeId(request.getIssueTypeId())
			.statusId(request.getStatusId())
			.priority(request.getPriority())
			.storyPnt(request.getStoryPnt())
			.assigneeId(request.getAssigneeId())
			.reporterId(reporter.getObjId())
			.dueDt(request.getDueDt())
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("CREATE-ISSUE")
			.useStatCd(UseStatCd.Usable)
			.evtNm("CreateIssue")
			.prevEvntNm("None")
			.build();

		issueAccess.save(issue);

		// Record Log
		recordLog(issue.getObjId(), "Issue", null, "Created", reporter.getObjId());

		// Initial Position (Simple Rank)
		SnCiraIssuePositionModel position = SnCiraIssuePositionModel.builder()
			.issueId(issue.getObjId())
			.columnId("DEFAULT_COL") // TODO: Find actual column
			.rankStr("0|i00000:") // Very simple Lexorank placeholder
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("CREATE-ISSUE")
			.useStatCd(UseStatCd.Usable)
			.evtNm("SetInitialPosition")
			.prevEvntNm("None")
			.build();
		issuePositionAccess.save(position);

		return mapToResponse(issue);
	}

	public IssueResponse getIssue(String issueId) {
		SnCiraIssueModel issue = issueAccess.findById(issueId);
		
		if (issue.getDeletedAt() != null) {
			throw new EntityNotFoundException("Issue not found: " + issueId);
		}

		return mapToResponse(issue);
	}

	@Transactional
	public IssueResponse updateIssue(String issueId, UpdateIssueRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel user = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		SnCiraIssueModel issue = issueAccess.findById(issueId);

		// Check membership
		projectMemberAccess.findAllByUserId(user.getObjId()).stream()
			.filter(m -> m.getProjectId().equals(issue.getProjectId()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("User is not a member of the project"));

		// Record changes and update
		if (request.getTitle() != null && !request.getTitle().equals(issue.getTitle())) {
			recordLog(issueId, "TITLE", issue.getTitle(), request.getTitle(), user.getObjId());
			issue.setTitle(request.getTitle());
		}
		if (request.getContent() != null && !request.getContent().equals(issue.getContent())) {
			recordLog(issueId, "CONTENT", issue.getContent(), request.getContent(), user.getObjId());
			issue.setContent(request.getContent());
		}
		// ... repeat for other fields

		issueAccess.save(issue);
		return mapToResponse(issue);
	}

	@Transactional
	public void deleteIssue(String issueId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel user = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		SnCiraIssueModel issue = issueAccess.findById(issueId);

		// Check Admin membership
		SnCiraProjectMemberModel member = projectMemberAccess.findAllByUserId(user.getObjId()).stream()
			.filter(m -> m.getProjectId().equals(issue.getProjectId()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("User is not a member of the project"));

		if (!"ADMIN".equals(member.getRole())) {
			throw new IllegalArgumentException("Only Admin can delete issues");
		}

		issue.setDeletedAt(LocalDateTime.now());
		issueAccess.save(issue);
	}

	public Page<IssueResponse> listIssues(String projectId, IssueFilterRequest filter, Pageable pageable) {
		Specification<SnCiraIssueModel> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get("projectId"), projectId));
			predicates.add(cb.isNull(root.get("deletedAt")));

			if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
				predicates.add(root.get("statusId").in(filter.getStatus()));
			}
			if (filter.getPriority() != null) {
				predicates.add(cb.equal(root.get("priority"), filter.getPriority()));
			}
			if (filter.getAssigneeId() != null) {
				predicates.add(cb.equal(root.get("assigneeId"), filter.getAssigneeId()));
			}
			if (filter.getIssueType() != null) {
				predicates.add(cb.equal(root.get("issueTypeId"), filter.getIssueType()));
			}
			if (filter.getSprintId() != null) {
				predicates.add(cb.equal(root.get("sprintId"), filter.getSprintId()));
			}
			if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.getKeyword().toLowerCase() + "%"));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		return issueAccess.findAll(spec, pageable).map(this::mapToResponse);
	}

	private void recordLog(String issueId, String field, String oldVal, String newVal, String changerId) {
		SnCiraIssueLogModel log = SnCiraIssueLogModel.builder()
			.issueId(issueId)
			.fieldNm(field)
			.oldVal(oldVal)
			.newVal(newVal)
			.changedBy(changerId)
			.changedAt(LocalDateTime.now())
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("ISSUE-LOG")
			.useStatCd(UseStatCd.Usable)
			.evtNm("RecordLog")
			.prevEvntNm("None")
			.build();
		issueLogAccess.save(log);
	}

	private IssueResponse mapToResponse(SnCiraIssueModel model) {
		GsUserModel reporter = userAccess.findByIdOptional(model.getReporterId()).orElse(null);
		GsUserModel assignee = model.getAssigneeId() != null ? userAccess.findByIdOptional(model.getAssigneeId()).orElse(null) : null;
		
		SnCiraCiraIssueTypeModel type = issueTypeAccess.findByIdOptional(model.getIssueTypeId()).orElse(null);
		SnCiraIssueStatusModel status = statusAccess.findByIdOptional(model.getStatusId()).orElse(null);

		return IssueResponse.builder()
			.id(model.getObjId())
			.issueKey(model.getIssueKey())
			.title(model.getTitle())
			.content(model.getContent())
			.issueTypeId(model.getIssueTypeId())
			.issueTypeNm(type != null ? type.getTypeNm() : null)
			.statusId(model.getStatusId())
			.statusNm(status != null ? status.getStatusNm() : null)
			.priority(model.getPriority())
			.storyPnt(model.getStoryPnt())
			.reporter(reporter != null ? UserResponse.builder().email(reporter.getEmail()).name(reporter.getUserNm()).avatarUrl(reporter.getAvatarUrl()).build() : null)
			.assignee(assignee != null ? UserResponse.builder().email(assignee.getEmail()).name(assignee.getUserNm()).avatarUrl(assignee.getAvatarUrl()).build() : null)
			.projectId(model.getProjectId())
			.sprintId(model.getSprintId())
			.dueDt(model.getDueDt())
			.createdAt(model.getCreatedAt())
			.modifiedAt(model.getModifiedAt())
			.build();
	}
}
