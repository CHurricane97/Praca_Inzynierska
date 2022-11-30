package com.example.town_application.repository;

import com.example.town_application.model.ActionType;
import com.example.town_application.model.MotionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionTypeRepository  extends JpaRepository<ActionType, Integer> {
}
