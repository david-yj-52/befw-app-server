package com.tsh.starter.befw.app.server.apService.cira;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsh.starter.befw.app.server.apService.cira.dto.CreateProjectRequest;
import com.tsh.starter.befw.app.server.apService.cira.dto.ProjectResponse;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraBoard.SnCiraBoardAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraBoard.SnCiraBoardModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraBoardColumn.SnCiraBoardColumnAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraBoardColumn.SnCiraBoardColumnModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueStatus.SnCiraIssueStatusAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueStatus.SnCiraIssueStatusModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueTransition.SnCiraIssueTransitionAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssueTransition.SnCiraIssueTransitionModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProject.SnCiraProjectAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProject.SnCiraProjectModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember.SnCiraProjectMemberAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember.SnCiraProjectMemberModel;
import com.tsh.starter.befw.lib.core.config.ApplicationProperties;
import com.tsh.starter.befw.lib.core.data.constant.UseStatCd;
import com.tsh.starter.befw.lib.core.data.orm.usersAndPermissions.gsUser.GsUserAccess;
import com.tsh.starter.befw.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private final SnCiraProjectAccess projectAccess;
	private final SnCiraProjectMemberAccess projectMemberAccess;
	private final GsUserAccess userAccess;
	private final SnCiraIssueStatusAccess issueStatusAccess;
	private final SnCiraIssueTransitionAccess issueTransitionAccess;
	private final SnCiraBoardAccess boardAccess;
	private final SnCiraBoardColumnAccess boardColumnAccess;

	@Transactional
	public ProjectResponse createProject(CreateProjectRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel owner = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		if (projectAccess.findByProjectKey(request.getKey()).isPresent()) {
			throw new IllegalArgumentException("Project key already exists: " + request.getKey());
		}

		SnCiraProjectModel project = SnCiraProjectModel.builder()
			.projectKey(request.getKey())
			.projectNm(request.getName())
			.descr(request.getDescription())
			.projectType(request.getProjectType())
			.ownerId(owner.getObjId())
			.issueSequence(0)
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("INIT-PROJ")
			.useStatCd(UseStatCd.Usable)
			.evtNm("CreateProject")
			.prevEvntNm("None")
			.build();

		projectAccess.save(project);

		// Add owner as Admin member
		SnCiraProjectMemberModel member = SnCiraProjectMemberModel.builder()
			.projectId(project.getObjId())
			.userId(owner.getObjId())
			.role("ADMIN")
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("INIT-PROJ")
			.useStatCd(UseStatCd.Usable)
			.evtNm("AddOwner")
			.prevEvntNm("None")
			.build();

		projectMemberAccess.save(member);

		initDefaultWorkflow(project.getObjId(), request.getProjectType());

		return mapToResponse(project);
	}

	private void initDefaultWorkflow(String projectId, String projectType) {
		SnCiraIssueStatusModel todo = createStatus(projectId, "To Do", "TODO", "#DFE1E6", (short) 1);
		SnCiraIssueStatusModel inProgress = createStatus(projectId, "In Progress", "IN_PROGRESS", "#0052CC", (short) 2);
		SnCiraIssueStatusModel inReview = createStatus(projectId, "In Review", "IN_PROGRESS", "#FF991F", (short) 3);
		SnCiraIssueStatusModel done = createStatus(projectId, "Done", "DONE", "#00875A", (short) 4);

		createTransition(projectId, todo.getObjId(), inProgress.getObjId());
		createTransition(projectId, todo.getObjId(), done.getObjId());
		createTransition(projectId, inProgress.getObjId(), inReview.getObjId());
		createTransition(projectId, inProgress.getObjId(), todo.getObjId());
		createTransition(projectId, inReview.getObjId(), done.getObjId());
		createTransition(projectId, inReview.getObjId(), inProgress.getObjId());

		SnCiraBoardModel board = SnCiraBoardModel.builder()
			.projectId(projectId)
			.boardNm("Default Board")
			.boardType(projectType != null ? projectType : "KANBAN")
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("INIT-PROJ")
			.useStatCd(UseStatCd.Usable)
			.evtNm("CreateDefaultBoard")
			.prevEvntNm("None")
			.build();
		boardAccess.save(board);

		createBoardColumn(board.getObjId(), todo.getObjId(), "To Do", (short) 1);
		createBoardColumn(board.getObjId(), inProgress.getObjId(), "In Progress", (short) 2);
		createBoardColumn(board.getObjId(), inReview.getObjId(), "In Review", (short) 3);
		createBoardColumn(board.getObjId(), done.getObjId(), "Done", (short) 4);
	}

	private SnCiraIssueStatusModel createStatus(String projectId, String statusNm, String category, String colorCd, short sortOrd) {
		SnCiraIssueStatusModel status = SnCiraIssueStatusModel.builder()
			.projectId(projectId)
			.statusNm(statusNm)
			.category(category)
			.colorCd(colorCd)
			.sortOrd(sortOrd)
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("INIT-PROJ")
			.useStatCd(UseStatCd.Usable)
			.evtNm("CreateStatus")
			.prevEvntNm("None")
			.build();
		issueStatusAccess.save(status);
		return status;
	}

	private void createTransition(String projectId, String fromStatusId, String toStatusId) {
		SnCiraIssueTransitionModel transition = SnCiraIssueTransitionModel.builder()
			.projectId(projectId)
			.fromStatusId(fromStatusId)
			.toStatusId(toStatusId)
			.allowYn("Y")
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("INIT-PROJ")
			.useStatCd(UseStatCd.Usable)
			.evtNm("CreateTransition")
			.prevEvntNm("None")
			.build();
		issueTransitionAccess.save(transition);
	}

	private void createBoardColumn(String boardId, String statusId, String columnNm, short sortOrd) {
		SnCiraBoardColumnModel column = SnCiraBoardColumnModel.builder()
			.boardId(boardId)
			.statusId(statusId)
			.columnNm(columnNm)
			.sortOrd(sortOrd)
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("INIT-PROJ")
			.useStatCd(UseStatCd.Usable)
			.evtNm("CreateBoardColumn")
			.prevEvntNm("None")
			.build();
		boardColumnAccess.save(column);
	}

	public List<ProjectResponse> getMyProjects() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel user = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		// Find projects where user is a member
		List<SnCiraProjectMemberModel> memberships = projectMemberAccess.findAllByUserId(user.getObjId());
		List<String> projectIds = memberships.stream()
			.map(SnCiraProjectMemberModel::getProjectId)
			.collect(Collectors.toList());

		return projectAccess.findAllById(projectIds).stream()
			.filter(p -> p.getDeletedAt() == null)
			.map(this::mapToResponse)
			.collect(Collectors.toList());
	}

	public ProjectResponse getProject(String projectId) {
		SnCiraProjectModel project = projectAccess.findById(projectId);
		
		if (project.getDeletedAt() != null) {
			throw new EntityNotFoundException("Project not found: " + projectId);
		}

		return mapToResponse(project);
	}

	private ProjectResponse mapToResponse(SnCiraProjectModel model) {
		return ProjectResponse.builder()
			.id(model.getObjId())
			.key(model.getProjectKey())
			.name(model.getProjectNm())
			.description(model.getDescr())
			.projectType(model.getProjectType())
			.ownerId(model.getOwnerId())
			.issueSequence(model.getIssueSequence())
			.build();
	}
}
