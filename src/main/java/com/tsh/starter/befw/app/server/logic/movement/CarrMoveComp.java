package com.tsh.starter.befw.app.server.logic.movement;

import com.tsh.starter.befw.app.server.dataProvider.MovementDataProvider;

public class CarrMoveComp {

    private  final String carrId;
    private final  String jobId;
    private final MovementDataProvider movementDataProvider;

    public CarrMoveComp (String carrId, String jobId, MovementDataProvider movementDataProvider){
        this.carrId = carrId;
        this.jobId = jobId;
        this.movementDataProvider = movementDataProvider;

        // data CRUD 필요
    }
}
