package com.tsh.starter.befw.app.server.interfaces.controller;


import com.tsh.schema.ServerSampleMessage;
import com.tsh.schema.ServerSampleMessageBody;
import com.tsh.schema.ServerSampleMessageHead;

public class TestController {

    static void main() {

        ServerSampleMessage msg = ServerSampleMessage.builder()
                .head(ServerSampleMessageHead.builder()
                        .src(ServerSampleMessageHead.SrcEnum.AGENT)
                        .tgt(ServerSampleMessageHead.TgtEnum.AGENT)
                        .tid("TID")
                        .build())
                .body(ServerSampleMessageBody.builder()
                        .siteId("SITE")
                        .userId("User")
                        .useYn("Y")
                        .build())
                .build();

        System.out.println(msg);


    }
}
