package com.example.demo.service.impl;

import com.example.demo.data.Ticket;
import com.example.demo.dto.SeatDto;
import com.example.demo.dto.TicketDto;
import com.example.demo.dto.TimetableDto;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.TicketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final Integer MAX_SEAT_NUM = 3;
    private final Integer MAX_ROW_NUM = 3;
    private final Double BENEFIT_COST = 200.00;
    private final Double COMMON_COST = 310.00;

    @Override
    @Transactional
    public Ticket save(TicketDto ticketDto) {
        if (ticketDto.isBenefits()) {
            ticketDto.setCost(BENEFIT_COST);
        } else {
            ticketDto.setCost(COMMON_COST);
        }
        Ticket ticket = ticketDto.toEntity();
        ticketRepository.save(ticket);
        return ticket;
    }

    @Override
    public Ticket deleteById(Long id) {
        Ticket ticket = ticketRepository.findFirstById(id);
        ticketRepository.deleteById(id);
        return ticket;
    }

    @Override
    public void deleteAll() {
        ticketRepository.deleteAll();
    }

    @Override
    public TicketDto findById(Long id) {
        Ticket ticket = ticketRepository.findFirstById(id);
        return ticket == null ? null : ticket.toDto();
    }

    @Override
    public List<TicketDto> findAll() {
        return ticketRepository.findAll().stream().map(Ticket::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SeatDto> getFreeSeats(TimetableDto timetableDto) {
        List<SeatDto> freeSeats = new ArrayList<>();
        List<SeatDto> busySeats = ticketRepository
                .findAllByTimetable_Id(timetableDto.getId())
                .stream().map((e) -> new SeatDto(e.getSeat(), e.getRow()))
                .toList();
        for (int row = 1; row <= MAX_ROW_NUM; row++) {
            for (int seat = 1; seat <= MAX_SEAT_NUM; seat++) {
                SeatDto seatDto = new SeatDto(seat, row);
                if (! busySeats.contains(seatDto))
                    freeSeats.add(seatDto);
            }
        }
        return freeSeats;
    }
}
