package com.example.town_application.repository;

import com.example.town_application.model.LoginRegister;
import com.example.town_application.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRegisterRepository extends JpaRepository<LoginRegister, Integer> {
}
