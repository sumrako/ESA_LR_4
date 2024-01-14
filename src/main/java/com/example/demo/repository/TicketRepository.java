package com.example.demo.repository;

import com.example.demo.data.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Ticket findFirstById(Long id);
    @Override
    List<Ticket> findAll();
    List<Ticket> findAllByTimetable_Id(Long timetableId);
}
