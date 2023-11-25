package com.thybak.bots.kkbot.adapter.outbound;

import com.thybak.bots.kkbot.adapter.outbound.jpa.JpaSecretionRepository;
import com.thybak.bots.kkbot.domain.model.Secretion;
import com.thybak.bots.kkbot.domain.provider.SecretionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class KkBotSecretionRepository implements SecretionRepository {
    private final JpaSecretionRepository jpaSecretionRepository;

    @Override
    public Secretion save(Secretion secretion) {
        return jpaSecretionRepository.save(secretion);
    }

    @Override
    public List<Secretion> findAllByTimestampBetweenAndChatId(Instant start, Instant end, Long chatId) {
        return jpaSecretionRepository.findAllByTimestampBetweenAndChatId(start, end, chatId);
    }
}
