package com.example.demo.repository;

import com.example.demo.data.Timetable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends CrudRepository<Timetable, Long> {
    Timetable findFirstById(Long id);
    @Override
    List<Timetable> findAll();
    List<Timetable> findAllByPerformance_Id(Long id);
}
