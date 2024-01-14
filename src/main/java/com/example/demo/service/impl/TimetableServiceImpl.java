package com.example.demo.service.impl;

import com.example.demo.data.Timetable;
import com.example.demo.dto.PerformanceDto;
import com.example.demo.dto.TimetableDto;
import com.example.demo.repository.TimetableRepository;
import com.example.demo.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private final TimetableRepository timetableRepository;
    @Override
    public void save(TimetableDto timetableDto) {
        timetableRepository.save(timetableDto.toEntity());
    }
    @Override
    public Timetable deleteById(Long id) {
        Timetable timetable = timetableRepository.findFirstById(id);
        timetableRepository.deleteById(id);
        return timetable;
    }
    @Override
    public void deleteAll() {
        timetableRepository.deleteAll();
    }
    @Override
    public TimetableDto findById(Long id) {
        Timetable timetable = timetableRepository.findFirstById(id);
        if (timetable == null)
            return null;
        return timetable.toDto();
    }
    @Override
    public List<TimetableDto> findAll() {
        return timetableRepository.findAll().stream().map(Timetable::toDto).collect(Collectors.toList());
    }
    @Override
    public List<TimetableDto> findAllByPerformance(PerformanceDto performanceDto) {
        return timetableRepository.findAllByPerformance_Id(performanceDto.getId())
                .stream().map(Timetable::toDto)
                .collect(Collectors.toList());
    }
}
