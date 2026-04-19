package com.tsh.starter.befw.app.server.interfaces.controller.mdm;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnReq;
import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnRes;
import com.tsh.starter.befw.lib.core.data.orm.common.access.CrudService;
import com.tsh.starter.befw.lib.core.data.orm.gnMsgSrvConn.GnMsgSrvConnAccess;
import com.tsh.starter.befw.lib.core.data.orm.gnMsgSrvConn.GnMsgSrvConnModel;
import com.tsh.starter.befw.lib.core.interfaces.AbstractCrudController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/mdm/config")
@RequiredArgsConstructor
@Slf4j
public class ConfigManageController
	extends AbstractCrudController<GnMsgSrvConnReq, GnMsgSrvConnRes, GnMsgSrvConnModel, String> {

	private final GnMsgSrvConnAccess gnMsgSrvConnAccess;

	@Override
	protected CrudService<GnMsgSrvConnModel, String> getService() {
		return gnMsgSrvConnAccess;
	}

	@Override
	protected GnMsgSrvConnModel toModel(GnMsgSrvConnReq req) {
		log.info("reqVo : {}", req);

		return GnMsgSrvConnModel.builder()
			.srvId(req.getSrvId())
			.tenant(req.getTenant())
			.evetNm(req.getEvetNm())
			.prevEventNm(req.getPrevEventNm())
			.actCm(req.getActCm())
			.actCd(req.getActCd())
			.solNm(req.getSolNm())
			.env(req.getEnv())
			.host(req.getHost())
			.port(req.getPort())
			.conn_user(req.getUser())
			.pwd(req.getPwd())
			.domain(req.getDomain())
			.traceId()
			.build();
	}

	@Override
	protected GnMsgSrvConnRes toResponse(GnMsgSrvConnModel model) {
		return GnMsgSrvConnRes.from(model);
	}
}
