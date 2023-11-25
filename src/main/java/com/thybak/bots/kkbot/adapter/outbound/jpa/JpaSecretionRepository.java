package com.thybak.bots.kkbot.adapter.outbound.jpa;

import com.thybak.bots.kkbot.domain.model.Secretion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface JpaSecretionRepository extends CrudRepository<Secretion, Long> {
    List<Secretion> findAllByTimestampBetweenAndChatId(Instant start, Instant end, Long chatId);
}
