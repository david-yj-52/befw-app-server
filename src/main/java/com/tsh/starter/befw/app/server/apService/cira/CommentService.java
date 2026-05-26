package com.tsh.starter.befw.app.server.apService.cira;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tsh.starter.befw.app.server.apService.cira.dto.CommentResponse;
import com.tsh.starter.befw.app.server.apService.cira.dto.CreateCommentRequest;
import com.tsh.starter.befw.app.server.apService.cira.dto.UpdateCommentRequest;
import com.tsh.starter.befw.app.server.apService.cira.exception.CiraException;
import com.tsh.starter.befw.app.server.apService.cira.exception.ErrorCode;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraComment.SnCiraCommentAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraComment.SnCiraCommentModel;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssue.SnCiraIssueAccess;
import com.tsh.starter.befw.app.server.data.orm.cira.ciraIssue.SnCiraIssueModel;
import com.tsh.starter.befw.lib.core.apService.auth.dto.UserResponse;
import com.tsh.starter.befw.lib.core.config.ApplicationProperties;
import com.tsh.starter.befw.lib.core.data.constant.UseStatCd;
import com.tsh.starter.befw.lib.core.data.orm.usersAndPermissions.gsUser.GsUserAccess;
import com.tsh.starter.befw.lib.core.data.orm.usersAndPermissions.gsUser.GsUserModel;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final SnCiraCommentAccess commentAccess;
	private final SnCiraIssueAccess issueAccess;
	private final GsUserAccess userAccess;

	@Transactional
	public CommentResponse createComment(String issueId, CreateCommentRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel author = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		SnCiraIssueModel issue = issueAccess.findById(issueId);
		if (issue.getDeletedAt() != null) {
			throw new CiraException(ErrorCode.ISSUE_NOT_FOUND, "이슈를 찾을 수 없습니다: " + issueId);
		}

		if (request.getParentId() != null) {
			commentAccess.findByIdOptional(request.getParentId())
				.filter(c -> UseStatCd.Usable.equals(c.getUseStatCd()))
				.orElseThrow(() -> new CiraException(ErrorCode.COMMENT_NOT_FOUND, "부모 댓글을 찾을 수 없습니다: " + request.getParentId()));
		}

		SnCiraCommentModel comment = SnCiraCommentModel.builder()
			.issueId(issueId)
			.authorId(author.getObjId())
			.parentId(request.getParentId())
			.content(request.getContent())
			.srvId(ApplicationProperties.getApplicationServiceName())
			.tenant(ApplicationProperties.getApplicationTenant())
			.traceId("CREATE-COMMENT")
			.useStatCd(UseStatCd.Usable)
			.evtNm("CreateComment")
			.prevEvntNm("None")
			.build();

		commentAccess.save(comment);
		return mapToResponse(comment, false);
	}

	public List<CommentResponse> getComments(String issueId) {
		SnCiraIssueModel issue = issueAccess.findById(issueId);
		if (issue.getDeletedAt() != null) {
			throw new CiraException(ErrorCode.ISSUE_NOT_FOUND, "이슈를 찾을 수 없습니다: " + issueId);
		}

		List<SnCiraCommentModel> all = commentAccess.findByIssueId(issueId);

		// parentId가 null인 루트 댓글만 추출 후 대댓글을 함께 조합
		return all.stream()
			.filter(c -> c.getParentId() == null && UseStatCd.Usable.equals(c.getUseStatCd()))
			.map(root -> {
				CommentResponse response = mapToResponse(root, false);
				List<CommentResponse> replies = all.stream()
					.filter(c -> root.getObjId().equals(c.getParentId()) && UseStatCd.Usable.equals(c.getUseStatCd()))
					.map(reply -> mapToResponse(reply, true))
					.collect(Collectors.toList());
				response.setReplies(replies);
				return response;
			})
			.collect(Collectors.toList());
	}

	@Transactional
	public CommentResponse updateComment(String commentId, UpdateCommentRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel user = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		SnCiraCommentModel comment = commentAccess.findById(commentId);
		if (!UseStatCd.Usable.equals(comment.getUseStatCd())) {
			throw new CiraException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다: " + commentId);
		}

		if (!comment.getAuthorId().equals(user.getObjId())) {
			throw new CiraException(ErrorCode.COMMENT_NOT_AUTHOR);
		}

		comment.setContent(request.getContent());
		comment.setEvtNm("UpdateComment");
		comment.setPrevEvntNm("CreateComment");
		commentAccess.save(comment);

		return mapToResponse(comment, comment.getParentId() != null);
	}

	@Transactional
	public void deleteComment(String commentId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		GsUserModel user = userAccess.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + email));

		SnCiraCommentModel comment = commentAccess.findById(commentId);
		if (!UseStatCd.Usable.equals(comment.getUseStatCd())) {
			throw new CiraException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다: " + commentId);
		}

		if (!comment.getAuthorId().equals(user.getObjId())) {
			throw new CiraException(ErrorCode.COMMENT_NOT_AUTHOR);
		}

		comment.setUseStatCd(UseStatCd.Delete);
		comment.setEvtNm("DeleteComment");
		comment.setPrevEvntNm("UpdateComment");
		commentAccess.save(comment);
	}

	private CommentResponse mapToResponse(SnCiraCommentModel model, boolean suppressReplies) {
		GsUserModel author = userAccess.findByIdOptional(model.getAuthorId()).orElse(null);

		return CommentResponse.builder()
			.id(model.getObjId())
			.issueId(model.getIssueId())
			.parentId(model.getParentId())
			.content(model.getContent())
			.author(author != null
				? UserResponse.builder()
					.email(author.getEmail())
					.name(author.getUserNm())
					.avatarUrl(author.getAvatarUrl())
					.build()
				: null)
			.replies(suppressReplies ? null : List.of())
			.createdAt(model.getCreatedAt())
			.modifiedAt(model.getModifiedAt())
			.build();
	}
}
