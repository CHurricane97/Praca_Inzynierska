package com.example.town_application.service;

import com.example.town_application.model.dto.MotionStateDTO;
import com.example.town_application.model.dto.MotionTypeDTO;
import com.example.town_application.repository.MotionTypeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MotionTypeService {
    private final MotionTypeRepository motionTypeRepository;
    private final ModelMapper modelMapper;
    public List<MotionTypeDTO> getAllMotionTypes() {
        return motionTypeRepository.findAll().stream()
                .map(warehouseItem -> modelMapper.map(warehouseItem, MotionTypeDTO.class))
                .collect(Collectors.toList());
    }
}
