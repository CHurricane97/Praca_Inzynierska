package com.example.town_application.service;

import com.example.town_application.model.AccountType;
import com.example.town_application.repository.OrderStatusEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TestServis {


    private final OrderStatusEntityRepository orderStatusRepository;

    public List<AccountType> getOrderStatuses(){
        return orderStatusRepository.findAll();
    }


}
