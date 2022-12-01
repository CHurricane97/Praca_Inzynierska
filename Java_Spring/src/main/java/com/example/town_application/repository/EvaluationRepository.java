package com.example.town_application.repository;

import com.example.town_application.model.Evaluation;
import com.example.town_application.model.MotionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository  extends JpaRepository<Evaluation, Integer> {
}
