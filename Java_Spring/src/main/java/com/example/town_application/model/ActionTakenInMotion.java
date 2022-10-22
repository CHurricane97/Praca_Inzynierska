package com.example.town_application.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "action_taken_in_motion", schema = "public", catalog = "Town_Database")
public class ActionTakenInMotion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "action_taken_in_motion_id")
    private int actionTakenInMotionId;
    @Basic
    @Column(name = "motion_id")
    private int motionId;
    @Basic
    @Column(name = "personal_data_id")
    private int personalDataId;
    @Basic
    @Column(name = "action_type_id")
    private int actionTypeId;

    public int getActionTakenInMotionId() {
        return actionTakenInMotionId;
    }

    public void setActionTakenInMotionId(int actionTakenInMotionId) {
        this.actionTakenInMotionId = actionTakenInMotionId;
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

    public int getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId = actionTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionTakenInMotion that = (ActionTakenInMotion) o;
        return actionTakenInMotionId == that.actionTakenInMotionId && motionId == that.motionId && personalDataId == that.personalDataId && actionTypeId == that.actionTypeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionTakenInMotionId, motionId, personalDataId, actionTypeId);
    }
}
