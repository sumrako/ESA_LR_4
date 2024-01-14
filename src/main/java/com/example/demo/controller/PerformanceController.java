package com.example.demo.controller;

import com.example.demo.data.Performance;
import com.example.demo.data.Log;
import com.example.demo.dto.PerformanceDto;
import com.example.demo.enums.ChangeTypeEnum;
import com.example.demo.enums.TablesEnum;
import com.example.demo.jms.Sender;
import com.example.demo.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceController {
    private final PerformanceService performanceService;
    private final Sender sender;

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("performances", performanceService.findAll());
        model.addAttribute("performance", new PerformanceDto());
        return "all-performances";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("performance", performanceService.findById(id));
        return "update-performance";
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute PerformanceDto performanceDto, Model model) {
        performanceDto.setId(id);
        performanceService.save(performanceDto);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.UPDATE)
                        .table(TablesEnum.PERFORMANCES)
                        .value(performanceDto.toEntity().toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/performance";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Performance performance = performanceService.deleteById(id);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.CASCADE_DELETE)
                        .table(TablesEnum.PERFORMANCES)
                        .value(performance.toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/performance";
    }

    @PostMapping("")
    public String create(@ModelAttribute PerformanceDto performanceDto, Model model) {
        performanceService.save(performanceDto);
        sender.send(
                Log.builder()
                        .changeType(ChangeTypeEnum.INSERT)
                        .table(TablesEnum.PERFORMANCES)
                        .value(performanceDto.toEntity().toString())
                        .datetime(LocalDateTime.now())
                        .build()
        );
        return "redirect:/performance";
    }


}
