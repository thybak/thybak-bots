package com.thybak.bots.kkbot.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PooRepository extends CrudRepository<Poo, Long> {
    List<Poo> findAllByTimestampBetweenAndChatId(Instant start, Instant end, Long chatId);
}
