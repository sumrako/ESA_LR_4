package com.example.demo.dto;

import com.example.demo.data.Performance;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PerformanceDto implements Serializable {
    private Long id;
    private String name;
    private String genre;
    private String country;

    public Performance toEntity() {
        return new Performance(this.id, this.name, this.genre, this.country);
    }
}

