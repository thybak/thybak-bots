package com.thybak.bots.kkbot.domain.provider;

import com.thybak.bots.kkbot.domain.model.Secretion;

import java.time.Instant;
import java.util.List;

public interface SecretionRepository {
    Secretion save(Secretion secretion);

    List<Secretion> findAllByTimestampBetweenAndChatId(Instant start, Instant end, Long chatId);
}
