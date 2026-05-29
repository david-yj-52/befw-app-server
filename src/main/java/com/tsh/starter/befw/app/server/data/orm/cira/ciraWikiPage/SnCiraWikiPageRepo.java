package com.tsh.starter.befw.app.server.data.orm.cira.ciraWikiPage;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.constant.UseStatCd;
import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraWikiPageRepo extends BaseJpaRepository<SnCiraWikiPageModel, String> {

	List<SnCiraWikiPageModel> findByProjectIdAndUseStatCdNotOrderBySortOrderAsc(String projectId, UseStatCd useStatCd);

	List<SnCiraWikiPageModel> findByParentIdAndUseStatCdNotOrderBySortOrderAsc(String parentId, UseStatCd useStatCd);
}
