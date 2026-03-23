package com.tsh.starter.befw.app.server.vo;


public record ServerSampleMessageIvo (
        MessageHeadIvo head,
        Body body

){
    public record Body(
            String siteId,
            String userId
    ){}
}
