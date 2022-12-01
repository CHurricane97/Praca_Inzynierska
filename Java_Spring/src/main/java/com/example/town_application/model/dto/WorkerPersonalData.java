package com.example.town_application.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class WorkerPersonalData {

    private int personalDataId;

    private String name;

    private String surname;

    private List<ActionName> actionTakenInMotionsByPersonalDataId;

}
