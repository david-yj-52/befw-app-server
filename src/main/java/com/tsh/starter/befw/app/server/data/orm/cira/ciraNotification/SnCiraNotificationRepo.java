package com.tsh.starter.befw.app.server.data.orm.cira.ciraNotification;

import org.springframework.stereotype.Repository;

import com.tsh.starter.befw.lib.core.data.orm.common.repo.BaseJpaRepository;

@Repository
public interface SnCiraNotificationRepo extends BaseJpaRepository<SnCiraNotificationModel, String> {
}
