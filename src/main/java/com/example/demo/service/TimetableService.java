package com.example.demo.service;

import com.example.demo.data.Timetable;
import com.example.demo.dto.PerformanceDto;
import com.example.demo.dto.TimetableDto;

import java.util.List;

public interface  TimetableService {
    void save(TimetableDto timetableDto);
    Timetable deleteById(Long id);
    void deleteAll();
    TimetableDto findById(Long id);
    List<TimetableDto> findAll();
    List<TimetableDto> findAllByPerformance(PerformanceDto performanceDto);
}
