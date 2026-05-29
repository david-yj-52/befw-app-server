package com.tsh.starter.befw.app.server.data.orm.cira.ciraCommentReaction;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraCommentReactionRepo extends BaseJpaRepository<SnCiraCommentReactionModel, String> {

	List<SnCiraCommentReactionModel> findByCommentId(String commentId);

	Optional<SnCiraCommentReactionModel> findByCommentIdAndUserIdAndReactionType(
		String commentId, String userId, String reactionType);

}
