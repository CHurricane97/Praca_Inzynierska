package com.example.town_application.WIP.requests.motion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMotionStateRequest {

    private Integer motionid;
    private Integer newmotionstate;

}
