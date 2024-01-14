package com.example.demo.repository;

import com.example.demo.data.Performance;
import com.example.demo.data.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    @Override
    List<Subscription> findAll();
}
