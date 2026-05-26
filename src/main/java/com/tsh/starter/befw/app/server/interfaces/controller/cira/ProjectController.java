package com.tsh.starter.befw.app.server.interfaces.controller.cira;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.app.server.apService.cira.ProjectService;
import com.tsh.starter.befw.app.server.apService.cira.dto.CreateProjectRequest;
import com.tsh.starter.befw.app.server.apService.cira.dto.ProjectResponse;
import com.tsh.starter.befw.lib.core.interfaces.rest.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping
	public ApiResponse<ProjectResponse> createProject(@RequestBody CreateProjectRequest request) {
		return ApiResponse.ok(projectService.createProject(request));
	}

	@GetMapping
	public ApiResponse<List<ProjectResponse>> getMyProjects() {
		return ApiResponse.ok(projectService.getMyProjects());
	}

	@GetMapping("/{projectId}")
	public ApiResponse<ProjectResponse> getProject(@PathVariable String projectId) {
		return ApiResponse.ok(projectService.getProject(projectId));
	}
}
