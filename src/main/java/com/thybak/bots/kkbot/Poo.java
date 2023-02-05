package com.thybak.bots.kkbot;

import jakarta.persistence.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.time.Instant;

@ToString
@RequiredArgsConstructor
@Entity
public class Poo {
    @Id
    @SequenceGenerator(name = "dog_sequence", sequenceName = "poo_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_sequence")
    @Column(updatable = false)
    private long Id;

    @NonNull
    @Column(nullable = false)
    private String username;

    @NonNull
    @Column(nullable = false)
    private Instant timestamp;

    @NonNull
    @Column(nullable = false)
    private Long chatId;
}
