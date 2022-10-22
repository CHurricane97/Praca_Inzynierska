package com.example.town_application.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "motion_state", schema = "public", catalog = "Town_Database")
public class MotionState {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "motion_state_id")
    private int motionStateId;
    @Basic
    @Column(name = "state")
    private String state;

    public int getMotionStateId() {
        return motionStateId;
    }

    public void setMotionStateId(int motionStateId) {
        this.motionStateId = motionStateId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotionState that = (MotionState) o;
        return motionStateId == that.motionStateId && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motionStateId, state);
    }
}
