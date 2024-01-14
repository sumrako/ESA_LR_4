package com.example.demo.controller;

import com.example.demo.data.Log;
import com.example.demo.data.Timetable;
import com.example.demo.dto.PerformanceDto;
import com.example.demo.dto.TimetableDto;
import com.example.demo.enums.ChangeTypeEnum;
import com.example.demo.enums.TablesEnum;
import com.example.demo.jms.Sender;
import com.example.demo.service.PerformanceService;
import com.example.demo.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timetable")
public class TimetableController {
    private final TimetableService timetableService;
    private final PerformanceService performanceService;
    private final Sender sender;


    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("performances", performanceService.findAll());
        model.addAttribute("timetable", new TimetableDto());
        model.addAttribute("fullTimetable",timetableService.findAll());
        return "full-timetable";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("performances", performanceService.findAll());
        model.addAttribute("timetable", timetableService.findById(id));
        return "update-timetable";
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute TimetableDto timetableDto, Model model) {
        timetableDto.setId(id);
        PerformanceDto performanceDto = performanceService.findById(timetableDto.getPerformance().getId());
        timetableDto.setPerformance(performanceDto);
        timetableService.save(timetableDto);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.UPDATE)
                        .table(TablesEnum.TIMETABLE)
                        .value(timetableDto.toEntity().toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/timetable";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Timetable timetable = timetableService.deleteById(id);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.CASCADE_DELETE)
                        .table(TablesEnum.TIMETABLE)
                        .value(timetable.toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/timetable";
    }

    @PostMapping("")
    public String create(@ModelAttribute TimetableDto timetableDto, Model model) {
        PerformanceDto performanceDto = performanceService.findById(timetableDto.getPerformance().getId());
        timetableDto.setPerformance(performanceDto);
        timetableService.save(timetableDto);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.INSERT)
                        .table(TablesEnum.TIMETABLE)
                        .value(timetableDto.toEntity().toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/timetable";
    }

}
