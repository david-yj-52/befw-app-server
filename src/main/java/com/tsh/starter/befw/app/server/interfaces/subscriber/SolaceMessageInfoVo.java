package com.tsh.starter.befw.app.server.interfaces.subscriber;

import com.solacesystems.jcsmp.BytesXMLMessage;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class SolaceMessageInfoVo {

	BytesXMLMessage msgObject;
	String responseTopic;
	String selectorKey;

}
