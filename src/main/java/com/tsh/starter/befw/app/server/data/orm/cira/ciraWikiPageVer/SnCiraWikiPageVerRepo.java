package com.tsh.starter.befw.app.server.data.orm.cira.ciraWikiPageVer;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraWikiPageVerRepo extends BaseJpaRepository<SnCiraWikiPageVerModel, String> {

	List<SnCiraWikiPageVerModel> findByPageIdOrderByVersionDesc(String pageId);
}
