package com.example.demo.repository;

import com.example.demo.data.Log;
import org.springframework.data.repository.CrudRepository;

public interface LogRepository extends CrudRepository<Log, Long> {
}
