package com.example.town_application.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "evaluation", schema = "public", catalog = "Town_Database")
public class Evaluation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "evaluation_id")
    private int evaluationId;
    @Basic
    @Column(name = "motion_id")
    private int motionId;
    @Basic
    @Column(name = "personal_data_id")
    private int personalDataId;
    @Basic
    @Column(name = "grade")
    private int grade;
    @Basic
    @Column(name = "description")
    private String description;

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getMotionId() {
        return motionId;
    }

    public void setMotionId(int motionId) {
        this.motionId = motionId;
    }

    public int getPersonalDataId() {
        return personalDataId;
    }

    public void setPersonalDataId(int personalDataId) {
        this.personalDataId = personalDataId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluation that = (Evaluation) o;
        return evaluationId == that.evaluationId && motionId == that.motionId && personalDataId == that.personalDataId && grade == that.grade && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaluationId, motionId, personalDataId, grade, description);
    }
}
