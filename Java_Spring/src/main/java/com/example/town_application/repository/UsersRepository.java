package com.example.town_application.repository;

import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByLogin(String login);

    Boolean existsByLogin(String login);

    Boolean existsByPermissionLevelAndPersonalDataForUsers(String permissionLevel, PersonalData personalDataForUsers);
}

