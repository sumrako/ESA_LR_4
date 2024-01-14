package com.example.demo.service;

import com.example.demo.data.Performance;
import com.example.demo.dto.PerformanceDto;

import java.util.List;

public interface PerformanceService {
    void save(PerformanceDto performanceDto);
    Performance deleteById(Long id);
    PerformanceDto findById(Long id);
    List<PerformanceDto> findAll();

}
