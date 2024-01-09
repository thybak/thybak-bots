package com.thybak.bots.kkbot.domain.usecase;

import com.thybak.bots.kkbot.domain.model.Secretion;
import com.thybak.bots.kkbot.domain.model.SecretionRankEntry;
import com.thybak.bots.kkbot.domain.model.SecretionRankPeriod;
import com.thybak.bots.kkbot.domain.model.SecretionType;
import com.thybak.bots.kkbot.domain.provider.SecretionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetSecretionRankingUseCase {
    private final Logger logger = LoggerFactory.getLogger(GetSecretionRankingUseCase.class);

    private final SecretionRepository secretionRepository;
    private final Clock clock;

    public List<SecretionRankEntry> run(SecretionRankPeriod secretionRankPeriod, Long chatId) {
        logger.info("Cálculo de ranking teniendo en cuenta el siguiente periodo comprendido entre {} - {}", secretionRankPeriod.initialInstant(clock), secretionRankPeriod.finalInstant(clock));
        final List<Secretion> secretions = secretionRepository.findAllByTimestampBetweenAndChatId(secretionRankPeriod.initialInstant(clock), secretionRankPeriod.finalInstant(clock), chatId);
        return getRankEntriesFrom(secretions.stream().collect(Collectors.groupingBy(Secretion::getUsername)));
    }

    private List<SecretionRankEntry> getRankEntriesFrom(Map<String, List<Secretion>> rankingUnsorted) {
        final List<SecretionRankEntry> rankEntries = new ArrayList<>();
        rankingUnsorted.forEach((String username, List<Secretion> secretionsByUser) ->
                rankEntries.add(new SecretionRankEntry
                        (username,
                        secretionsByUser.stream().filter(secretion -> secretion.getSecretionType() == SecretionType.POO).count(),
                        secretionsByUser.stream().filter(secretion -> secretion.getSecretionType() == SecretionType.PUKE).count())));
        return rankEntries;
    }


}
