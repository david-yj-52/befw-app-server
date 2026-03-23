package com.tsh.starter.befw.app.server.vo;

public record MessageHeadIvo(
        String src,
        String tgt,
        String event,
        String tid
) {}
