package com.tsh.starter.befw.app.server.apService.mdm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsh.starter.befw.app.server.interfaces.controller.mdm.dto.GnMsgSrvConnRes;
import com.tsh.starter.befw.lib.core.data.orm.gnMsgSrvConn.GnMsgSrvConnAccess;
import com.tsh.starter.befw.lib.core.interfaces.ApiResponse;
import com.tsh.starter.befw.lib.core.spec.in.AddMsgServerInf;

import lombok.extern.slf4j.Slf4j;

/**
 * BIZ 서비스는
 */
@Service
@Slf4j
public class MessageServerConfigApService {

	@Autowired
	GnMsgSrvConnAccess gnMsgSrvConnAccess;

	public ApiResponse<GnMsgSrvConnRes> generateMessageServerData(AddMsgServerInf ivo) {

		// this.gnMsgSrvConnAccess.create()
		return null;

	}
}
