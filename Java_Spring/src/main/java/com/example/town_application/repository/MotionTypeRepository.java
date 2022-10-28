package com.example.town_application.repository;

import com.example.town_application.model.MotionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotionTypeRepository extends JpaRepository<MotionType, Integer> {
}