package com.example.town_application.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "action_type", schema = "public", catalog = "Town_Database")
public class ActionType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "action_type_id")
    private int actionTypeId;
    @Basic
    @Column(name = "type")
    private String type;

    public int getActionTypeId() {
        return actionTypeId;
    }

    public void setActionTypeId(int actionTypeId) {
        this.actionTypeId = actionTypeId;
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
        ActionType that = (ActionType) o;
        return actionTypeId == that.actionTypeId && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionTypeId, type);
    }
}
