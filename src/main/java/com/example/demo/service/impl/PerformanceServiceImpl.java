package com.example.demo.service.impl;


import com.example.demo.data.Performance;
import com.example.demo.dto.PerformanceDto;
import com.example.demo.repository.PerformanceRepository;
import com.example.demo.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceServiceImpl implements PerformanceService {
    private final PerformanceRepository performanceRepository;

    @Override
    public void save(PerformanceDto performanceDto) {
        performanceRepository.save(performanceDto.toEntity());
    }

    @Override
    public Performance deleteById(Long id) {
        Performance performance = performanceRepository.findFirstById(id);
        performanceRepository.deleteById(id);
        return performance;
    }

    @Override
    public PerformanceDto findById(Long id) {
        Performance performance = performanceRepository.findFirstById(id);
        return performance == null ? null : performance.toDto();
    }

    @Override
    public List<PerformanceDto> findAll() {
        return performanceRepository.findAll().stream().map(Performance::toDto).collect(Collectors.toList());
    }
}
