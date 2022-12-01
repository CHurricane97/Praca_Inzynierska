package com.example.town_application.WIP.requests.evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AddEvaluationRequest {

    private Integer workerId;
    private Integer motionId;
    @Min(1)
    @Max(10)
    private Integer grade;
    @Size(max = 1023)
    private String description;

}
