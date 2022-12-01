package com.example.town_application.model.dto;

import com.example.town_application.model.Motion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionName {
    private String type;

    private MotionIDOnly motionForActionInMotions;

}
