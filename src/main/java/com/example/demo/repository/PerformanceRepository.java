package com.example.demo.repository;

import com.example.demo.data.Performance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceRepository extends CrudRepository<Performance, Long> {
    Performance findFirstById(Long id);
    @Override
    List<Performance> findAll();
}
