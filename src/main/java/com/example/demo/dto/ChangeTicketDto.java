package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChangeTicketDto {
    private Long id;
    private String seatDto;
    private TimetableDto timetable;
}
