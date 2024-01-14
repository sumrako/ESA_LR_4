package com.example.demo.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "email")
    private String email;

}
