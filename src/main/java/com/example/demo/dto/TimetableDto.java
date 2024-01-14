package com.example.demo.dto;

import com.example.demo.data.Timetable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TimetableDto implements Serializable {
    private Long id;
    private PerformanceDto performance;
    private int hall;
    private LocalDateTime date;
    public Timetable toEntity(){
        return new Timetable(this.id, performance.toEntity(), hall, date);
    }
}
