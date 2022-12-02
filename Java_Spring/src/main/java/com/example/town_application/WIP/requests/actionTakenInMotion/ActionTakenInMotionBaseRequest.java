package com.example.town_application.WIP.requests.actionTakenInMotion;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class ActionTakenInMotionBaseRequest {
    @Min(1)
Integer motionID;
}
