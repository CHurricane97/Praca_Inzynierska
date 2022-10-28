package com.example.town_application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Evaluation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "evaluation_id", nullable = false)
    private int evaluationId;
    @Basic
    @Column(name = "motion_id", nullable = false)
    private int motionId;
    @Basic
    @Column(name = "personal_data_id", nullable = false)
    private int personalDataId;
    @Basic
    @Column(name = "grade", nullable = false)
    private int grade;
    @Basic
    @Column(name = "description", nullable = true, length = 1023)
    private String description;
    @ManyToOne(targetEntity = Motion.class)
    @JoinColumn(name = "motion_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Motion motionForEvaluation;
    @ManyToOne(targetEntity = PersonalData.class)
    @JoinColumn(name = "personal_data_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PersonalData personalDataForEvaluation;


}