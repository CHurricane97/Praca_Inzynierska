package com.example.town_application.repository;

import com.example.town_application.model.Motion;
import com.example.town_application.model.MotionState;
import com.example.town_application.model.MotionType;
import com.example.town_application.model.PersonalData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotionRepository  extends JpaRepository<Motion, Integer> {

    List<Motion>findAllByPersonalDataForMotionsAndMotionStateByMotionStateIdOrMotionStateByMotionStateId(PersonalData personalData,
    MotionState motionState, MotionState motionState2, PageRequest pageRequest);
    List<Motion>findAllByPersonalDataForMotions(PersonalData personalData, PageRequest pageRequest);

    List<Motion> findByMotionStateByMotionStateId_StateNotAndMotionStateByMotionStateId_StateNot(String state, String state1, PageRequest pageRequest);




}
