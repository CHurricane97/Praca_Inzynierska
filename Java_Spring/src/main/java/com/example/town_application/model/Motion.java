package com.example.town_application.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "motion", schema = "public", catalog = "Town_Database")
public class Motion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "motion_id")
    private int motionId;
    @Basic
    @Column(name = "personal_data_id")
    private int personalDataId;
    @Basic
    @Column(name = "motion_type_id")
    private int motionTypeId;
    @Basic
    @Column(name = "motion_state_id")
    private int motionStateId;

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

    public int getMotionTypeId() {
        return motionTypeId;
    }

    public void setMotionTypeId(int motionTypeId) {
        this.motionTypeId = motionTypeId;
    }

    public int getMotionStateId() {
        return motionStateId;
    }

    public void setMotionStateId(int motionStateId) {
        this.motionStateId = motionStateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Motion that = (Motion) o;
        return motionId == that.motionId && personalDataId == that.personalDataId && motionTypeId == that.motionTypeId && motionStateId == that.motionStateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(motionId, personalDataId, motionTypeId, motionStateId);
    }
}
