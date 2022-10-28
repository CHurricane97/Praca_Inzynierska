package com.example.town_application.repository;

import com.example.town_application.model.MotionType;
import com.example.town_application.model.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Integer> {
}
