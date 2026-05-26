package com.tsh.starter.befw.app.server.data.orm.cira.ciraBoardColumn;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraBoardColumnRepo extends BaseJpaRepository<SnCiraBoardColumnModel, String> {

    List<SnCiraBoardColumnModel> findByStatusId(String statusId);

    Optional<SnCiraBoardColumnModel> findByBoardIdAndStatusId(String boardId, String statusId);

    List<SnCiraBoardColumnModel> findByBoardIdOrderBySortOrd(String boardId);
}
