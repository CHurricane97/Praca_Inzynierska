package com.example.town_application.repository;

import com.example.town_application.model.MotionType;
import com.example.town_application.model.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Integer> {

    Optional<PersonalData> findByPesel(String pesel);

    Optional<PersonalData>findByUsersByPersonalDataId(Integer userID);

    boolean existsByPesel(String pesel);

    Optional<PersonalData> findByPersonalDataId(int personalDataId);

}
