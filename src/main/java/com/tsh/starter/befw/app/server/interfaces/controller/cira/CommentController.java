package com.tsh.starter.befw.app.server.interfaces.controller.cira;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.app.server.apService.cira.CommentService;
import com.tsh.starter.befw.app.server.apService.cira.dto.CommentResponse;
import com.tsh.starter.befw.app.server.apService.cira.dto.CreateCommentRequest;
import com.tsh.starter.befw.app.server.apService.cira.dto.UpdateCommentRequest;
import com.tsh.starter.befw.lib.core.interfaces.rest.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping("/issues/{issueId}/comments")
	public ApiResponse<CommentResponse> createComment(
		@PathVariable String issueId,
		@Valid @RequestBody CreateCommentRequest request
	) {
		return ApiResponse.created(commentService.createComment(issueId, request));
	}

	@GetMapping("/issues/{issueId}/comments")
	public ApiResponse<List<CommentResponse>> getComments(@PathVariable String issueId) {
		return ApiResponse.ok(commentService.getComments(issueId));
	}

	@PutMapping("/comments/{commentId}")
	public ApiResponse<CommentResponse> updateComment(
		@PathVariable String commentId,
		@Valid @RequestBody UpdateCommentRequest request
	) {
		return ApiResponse.ok(commentService.updateComment(commentId, request));
	}

	@DeleteMapping("/comments/{commentId}")
	public ApiResponse<Void> deleteComment(@PathVariable String commentId) {
		commentService.deleteComment(commentId);
		return ApiResponse.noContent();
	}
}
