package com.tsh.starter.befw.app.server.apService;

import com.tsh.starter.befw.app.server.ApProcessVo;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.interfaces.InterfaceType;
import com.tsh.starter.befw.lib.core.spec.ApMessageBody;
import com.tsh.starter.befw.lib.core.spec.constant.ApMessageList;

public interface ApService<R, T extends ApMessageBody> {

	ApiResponse<R> run(ApProcessVo<T> procVo);

	// CompletableFuture<ApiResponse<R>> runAsync(ApProcessVo<T> procVo);

	ApMessageList getSupportedEvent();

	void handle(String payload, InterfaceType interfaceType) throws Exception;
}
