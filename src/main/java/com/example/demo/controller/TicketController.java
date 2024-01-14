package com.example.demo.controller;

import com.example.demo.data.Log;
import com.example.demo.data.Ticket;
import com.example.demo.dto.*;
import com.example.demo.enums.ChangeTypeEnum;
import com.example.demo.enums.TablesEnum;
import com.example.demo.jms.Sender;
import com.example.demo.service.PerformanceService;
import com.example.demo.service.TicketService;
import com.example.demo.service.TimetableService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {
    private final Sender sender;

    private final TimetableService timetableService;
    private final PerformanceService performanceService;
    private final TicketService ticketService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("")
    public String getAll(Model model) {
        List<PerformanceDto> performanceDtoList = performanceService.findAll();
        List<TimetableDto> timetableDtoList = new ArrayList<>();
        model.addAttribute("ticket", new TicketDto());
        model.addAttribute("changeTicket", new ChangeTicketDto());
        if (! performanceDtoList.isEmpty()) {
            timetableDtoList = timetableService.findAllByPerformance(performanceDtoList.get(0));
            if (!timetableDtoList.isEmpty()) {
                List<SeatDto> seatDtos = ticketService.getFreeSeats(timetableDtoList.get(0));
                if (!seatDtos.isEmpty()) {
                    model.addAttribute("ticket", new TicketDto(null, seatDtos.get(0),
                            0.0, false, timetableDtoList.get(0)));
                    model.addAttribute("changeTicket", new ChangeTicketDto(null, seatDtos.get(0).toString(),
                            timetableDtoList.get(0)));
                    model.addAttribute("seats", seatDtos);
                }
            }
        }
        model.addAttribute("performances", performanceDtoList);
        model.addAttribute("performance", new PerformanceDto());
        model.addAttribute("tickets", ticketService.findAll());
        model.addAttribute("fullTimetable", timetableDtoList);

        return "all-tickets";
    }

    @PostMapping("/performance")
    public String updateAllByPerformanceChange(@ModelAttribute PerformanceDto performanceDto, Model model) {
        updateAllByPerformance(performanceDto, model);
        return "all-tickets";
    }


    @PostMapping("/timetable")
    public String updateAllBySeanceChange(@ModelAttribute ChangeTicketDto changeTicketDto, Model model) {
        updateAllBySeance(changeTicketDto, model);
        return "all-tickets";
    }

    @PostMapping("/update/timetable")
    public String updateAllBySeanceChangeForUpdate(@ModelAttribute ChangeTicketDto changeTicketDto, Model model) {
        updateAllBySeance(changeTicketDto, model);
        return "update-ticket";
    }

    @PostMapping("")
    public String saveTicket(@ModelAttribute TicketDto ticketDto, Model model) {
        TimetableDto timetableDto = timetableService.findById(ticketDto.getTimetable().getId());
        ticketDto.setTimetable(timetableDto);
        Ticket ticket = ticketService.save(ticketDto);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.INSERT)
                        .table(TablesEnum.TICKETS)
                        .value(ticket.toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/ticket";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Ticket ticket = ticketService.deleteById(id);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.DELETE)
                        .table(TablesEnum.TICKETS)
                        .value(ticket.toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/ticket";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        TicketDto ticketDto = ticketService.findById(id);
        model.addAttribute("ticket", ticketDto);
        List<PerformanceDto> performanceDtoList = performanceService.findAll();
        model.addAttribute("changeTicket", new ChangeTicketDto(ticketDto.getId(), ticketDto.getSeatDto().toString(), ticketDto.getTimetable()));
        model.addAttribute("performance", ticketDto.getTimetable().getPerformance());
        List<TimetableDto> timetableDtoList = timetableService.findAllByPerformance(performanceService.findById(ticketDto.getTimetable().getPerformance().getId()));
        model.addAttribute("fullTimetable", timetableDtoList);
        List<SeatDto> seatDtos = new ArrayList<>();
        seatDtos.add(ticketDto.getSeatDto());
        seatDtos.addAll(ticketService.getFreeSeats(ticketDto.getTimetable()));
        model.addAttribute("seats", seatDtos);
        return "update-ticket";
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute TicketDto ticketDto, Model model) {
        ticketDto.setId(id);
        TimetableDto timetableDto = timetableService.findById(ticketDto.getTimetable().getId());
        ticketDto.setTimetable(timetableDto);
        Ticket ticket = ticketService.save(ticketDto);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.UPDATE)
                        .table(TablesEnum.TICKETS)
                        .value(ticket.toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/ticket";
    }


    private void updateAllBySeance(ChangeTicketDto changeTicketDto, Model model) {
        String[] seatsParams = changeTicketDto.getSeatDto().split(";");
        SeatDto seatDto = new SeatDto();
        if (seatsParams.length == 2) {
            seatDto = new SeatDto(Integer.parseInt(seatsParams[0]), Integer.parseInt(seatsParams[1]));
        }
        List<PerformanceDto> performanceDtoList = performanceService.findAll();
        TimetableDto timetableDto1 = timetableService.findById(changeTicketDto.getTimetable().getId());
        List<TimetableDto> timetableDtoList = timetableService.findAllByPerformance(performanceService.findById(timetableDto1.getPerformance().getId()));
        model.addAttribute("performances", performanceDtoList);
        model.addAttribute("performance", timetableDto1.getPerformance());
        model.addAttribute("ticket", new TicketDto(changeTicketDto.getId(), seatDto, 0.0, false, timetableDto1));
        model.addAttribute("tickets", ticketService.findAll());
        model.addAttribute("fullTimetable", timetableDtoList);
        model.addAttribute("changeTicket", new ChangeTicketDto(changeTicketDto.getId(), changeTicketDto.getSeatDto(), timetableDto1));
        model.addAttribute("seats",  ticketService.getFreeSeats(timetableDto1));
    }


    private void updateAllByPerformance(PerformanceDto performanceDto, Model model) {
        List<PerformanceDto> performanceDtoList = performanceService.findAll();
        model.addAttribute("ticket", new TicketDto());
        model.addAttribute("changeTicket", new ChangeTicketDto());
        List<TimetableDto> timetableDtoList = timetableService.findAllByPerformance(performanceService.findById(performanceDto.getId()));
        if (!timetableDtoList.isEmpty()) {
            List<SeatDto> seatDtos = ticketService.getFreeSeats(timetableDtoList.get(0));
            if (!seatDtos.isEmpty()) {
                model.addAttribute("ticket", new TicketDto(null, seatDtos.get(0),
                        0.0, false, timetableDtoList.get(0)));
                model.addAttribute("changeTicket", new ChangeTicketDto(null, seatDtos.get(0).toString(),
                        timetableDtoList.get(0)));
                model.addAttribute("seats", seatDtos);
            }
        }
        model.addAttribute("performances", performanceDtoList);
        model.addAttribute("performance", performanceDto);
        model.addAttribute("tickets", ticketService.findAll());
        model.addAttribute("fullTimetable", timetableDtoList);
    }
}
