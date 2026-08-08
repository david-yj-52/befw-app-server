package com.tsh.starter.befw.app.server.dataProvider;

import com.tsh.starter.befw.app.server.data.orm.cira.ciraAttachment.SnCiraAttachmentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MovementDataProvider {

    private final SnCiraAttachmentRepo snCiraAttachmentRepo;

    public MovementDataProvider(SnCiraAttachmentRepo snCiraAttachmentRepo){

        this.snCiraAttachmentRepo= snCiraAttachmentRepo;
    }






}
