package com.example.town_application.repository;

import com.example.town_application.model.Evaluation;
import com.example.town_application.model.MotionType;
import com.example.town_application.model.PersonalData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository  extends JpaRepository<Evaluation, Integer> {
    boolean existsByMotionForEvaluation_MotionIdAndPersonalDataForEvaluation_PersonalDataId(int motionId, int personalDataId);

    List<Evaluation> findByPersonalDataForEvaluation_PersonalDataId(int personalDataId, PageRequest pageRequest);

    List<Evaluation> findByMotionForEvaluation_PersonalDataForMotions(PersonalData personalDataForMotions, PageRequest pageRequest);


}
