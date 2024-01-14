package com.example.demo.dto;

import com.example.demo.data.Ticket;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TicketDto implements Serializable {
    private Long id;
    private SeatDto seatDto;
    private double cost;
    private boolean benefits;
    private TimetableDto timetable;

    public Ticket toEntity() {
        return new Ticket(this.id, this.seatDto.getSeat(), this.seatDto.getRow(), this.cost, this.timetable.toEntity());
    }

}

