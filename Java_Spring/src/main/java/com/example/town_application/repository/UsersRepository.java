package com.example.town_application.repository;

import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByLogin(String login);

    List<Users> findAll();

    List<Users> findByPersonalDataForUsers_PersonalDataId(int personalDataId);

    List<Users> findByPermissionLevel(String permissionLevel);

    Optional<Users> findByPersonalDataForUsers_Pesel(String pesel);

    Boolean existsByLoginAndPermissionLevel(String login, String permission_level);

    Boolean existsByPassword(String password);

    Boolean existsByPermissionLevel(String permission_level);

    Boolean existsByLogin(String login);

    Boolean existsByPermissionLevelAndPersonalDataForUsers(String permissionLevel, PersonalData personalDataForUsers);


}

