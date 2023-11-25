package com.thybak.bots.kkbot.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Secretion {
    @Id
    @SequenceGenerator(name = "secretion_sequence", sequenceName = "secretion_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secretion_sequence")
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

    @NonNull
    @Column(columnDefinition = "integer default 0")
    @Enumerated(EnumType.ORDINAL)
    private SecretionType secretionType;
}
