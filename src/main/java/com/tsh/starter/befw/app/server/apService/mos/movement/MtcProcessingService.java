/*
MTC 에서 호출된 메시지 받아 처리하는 로직을 제어하는 곳
 */
package com.tsh.starter.befw.app.server.apService.mos.movement;

import com.tsh.starter.befw.app.server.dataProvider.MovementDataProvider;
import com.tsh.starter.befw.app.server.logic.movement.CarrMoveComp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MtcProcessingService {

    private final MovementDataProvider movementDataProvider;

    public MtcProcessingService(MovementDataProvider movementDataProvider) {
        this.movementDataProvider = movementDataProvider;
    }

    private void calllogic(){
        log.info(" call the logic");

        CarrMoveComp carrMoveCompLogic = new CarrMoveComp("carId", "jobId", movementDataProvider);

    }
}
