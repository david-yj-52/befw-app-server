package com.tsh.starter.befw.app.server.data.orm.cira.ciraProjectMember;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraProjectMemberRepo extends BaseJpaRepository<SnCiraProjectMemberModel, String> {
}
