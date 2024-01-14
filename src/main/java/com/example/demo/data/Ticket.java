package com.example.demo.data;

import com.example.demo.dto.SeatDto;
import com.example.demo.dto.TicketDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tickets", schema = "lab2")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "seat")
    private int seat;

    @Column(name = "row")
    private int row;

    @Column(name = "cost")
    private double cost;

    @ManyToOne(targetEntity = Timetable.class, fetch = FetchType.EAGER, optional = false, cascade={CascadeType.MERGE, CascadeType.REFRESH})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    public TicketDto toDto() {
        return TicketDto.builder()
                .id(this.id)
                .cost(this.cost)
                .benefits(this.cost < 250)
                .seatDto(new SeatDto(this.seat, this.row))
                .timetable(this.timetable.toDto())
                .build();
    }

    @Override
    public String toString(){
        return "seat: " + seat + "; row: " + row + "; cost: " + cost + " timetable: (" + timetable.toString() + ")";
    }
}

