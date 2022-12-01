package com.example.town_application.repository;

import com.example.town_application.model.MotionType;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.dto.WorkerPersonalData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
@Repository
public interface PersonalDataRepository extends JpaRepository<PersonalData, Integer> {



    Optional<PersonalData> findByPesel(String pesel);

    Optional<PersonalData>findByUsersByPersonalDataId(Integer userID);

    boolean existsByPesel(String pesel);

    Optional<PersonalData> findByPersonalDataId(int personalDataId);

    List<PersonalData> findDistinctByActionTakenInMotionsByPersonalDataId_MotionForActionInMotions_MotionId(int motionId, PageRequest pageRequest);





}
