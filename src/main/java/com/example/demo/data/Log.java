package com.example.demo.data;

import com.example.demo.dto.PerformanceDto;
import com.example.demo.enums.ChangeTypeEnum;
import com.example.demo.enums.TablesEnum;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "log_changes")
public class Log implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "changeType")
    private ChangeTypeEnum changeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "tableName")
    private TablesEnum table;

    @Column(name = "value")
    private String value;

    @Column(name = "datetime")
    private LocalDateTime datetime;
}
