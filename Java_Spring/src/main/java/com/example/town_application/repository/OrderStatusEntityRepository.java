package com.example.town_application.repository;

import com.example.town_application.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface OrderStatusEntityRepository extends JpaRepository<AccountType, Integer> {
    }


