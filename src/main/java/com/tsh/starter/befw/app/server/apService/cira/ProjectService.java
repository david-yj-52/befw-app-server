package com.tsh.starter.befw.app.server.apService.cira;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsh.starter.befw.app.server.apService.cira.dto.CreateProjectRequest;
import com.tsh.starter.befw.app.server.apService.cira.dto.ProjectResponse;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProject.SnCiraProjectAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProject.SnCiraProjectModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember.SnCiraProjectMemberAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember.SnCiraProjectMemberModel;
import com.tsh.starter.befw.lib.core.apService.auth.UserService;
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

		return mapToResponse(project);
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
