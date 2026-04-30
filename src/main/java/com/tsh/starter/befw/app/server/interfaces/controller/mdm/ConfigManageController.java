package com.tsh.starter.befw.app.server.interfaces.controller.mdm;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnReq;
import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnRes;
import com.tsh.starter.befw.lib.core.data.orm.common.access.CrudService;
import com.tsh.starter.befw.lib.core.data.orm.msgServiceConn.gnMsgSrvConn.GsMsgSrvConnAccess;
import com.tsh.starter.befw.lib.core.data.orm.msgServiceConn.gnMsgSrvConn.GsMsgSrvConnModel;
import com.tsh.starter.befw.lib.core.interfaces.AbstractCrudController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/mdm/config")
@RequiredArgsConstructor
@Slf4j
public class ConfigManageController
	extends AbstractCrudController<GnMsgSrvConnReq, GnMsgSrvConnRes, GsMsgSrvConnModel, String> {

	private final GsMsgSrvConnAccess gsMsgSrvConnAccess;

	@Override
	protected CrudService<GsMsgSrvConnModel, String> getService() {
		return this.gsMsgSrvConnAccess;
	}

	@Override
	protected GsMsgSrvConnModel toModel(GnMsgSrvConnReq req) {
		log.info("reqVo : {}", req);

		return null;
	}

	@Override
	protected GnMsgSrvConnRes toResponse(GsMsgSrvConnModel model) {
		return GnMsgSrvConnRes.from(model);
	}
}
