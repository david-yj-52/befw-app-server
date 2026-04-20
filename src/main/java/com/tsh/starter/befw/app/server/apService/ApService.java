package com.tsh.starter.befw.app.server.apService;

import java.util.concurrent.CompletableFuture;

import com.tsh.starter.befw.app.server.ApProcessVo;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.spec.ApMessageBody;

public interface ApService<R, T extends ApMessageBody> {

	ApiResponse<R> run(ApProcessVo<T> procVo);

	CompletableFuture<ApiResponse<R>> runAsync(ApProcessVo<T> procVo);

}
