package com.example.town_application.repository;

import com.example.town_application.model.Motion;
import com.example.town_application.model.MotionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotionRepository  extends JpaRepository<Motion, Integer> {
}
