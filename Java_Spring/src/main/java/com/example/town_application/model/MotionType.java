package com.example.town_application.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "motion_type", schema = "public", catalog = "Town_Database")
public class MotionType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "motion_type_id")
    private int motionTypeId;
    @Basic
    @Column(name = "type")
    private String type;

    public int getMotionTypeId() {
        return motionTypeId;
    }

    public void setMotionTypeId(int motionTypeId) {
        this.motionTypeId = motionTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotionType that = (MotionType) o;
        return motionTypeId == that.motionTypeId && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(motionTypeId, type);
    }
}
