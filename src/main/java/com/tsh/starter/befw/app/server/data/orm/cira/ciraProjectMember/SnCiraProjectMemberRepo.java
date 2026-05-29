package com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember.SnCiraProjectMemberModel;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraProjectMemberRepo extends BaseJpaRepository<SnCiraProjectMemberModel, String> {

	List<SnCiraProjectMemberModel> findAllByUserId(String userId);

	List<SnCiraProjectMemberModel> findAllByProjectId(String projectId);

	Optional<SnCiraProjectMemberModel> findByProjectIdAndUserId(String projectId, String userId);
}
