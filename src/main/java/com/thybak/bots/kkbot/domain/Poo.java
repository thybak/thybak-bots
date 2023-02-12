package com.thybak.bots.kkbot.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Poo {
    @Id
    @SequenceGenerator(name = "poo_sequence", sequenceName = "poo_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poo_sequence")
    @Column(updatable = false)
    private long Id;

    @NonNull
    @Column(nullable = false)
    private String username;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Instant timestamp;

    @NonNull
    @Column(nullable = false)
    private Long chatId;
}
