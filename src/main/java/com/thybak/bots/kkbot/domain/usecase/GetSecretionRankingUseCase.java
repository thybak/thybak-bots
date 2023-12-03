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
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetSecretionRankingUseCase {
    private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";
    private static final ZoneId ZONE_ID = ZoneId.of(SPAIN_LOCAL_ZONE);
    private final Logger logger = LoggerFactory.getLogger(GetSecretionRankingUseCase.class);
    private final Clock clock;
    private final SecretionRepository secretionRepository;

    public List<SecretionRankEntry> run(SecretionRankPeriod secretionRankPeriod, Long chatId) {
        logger.info("Cálculo de ranking teniendo en cuenta el siguiente periodo comprendido entre {} - {}", getInitialInstantFrom(secretionRankPeriod), getFinalInstantFrom(secretionRankPeriod));
        final List<Secretion> secretions = secretionRepository.findAllByTimestampBetweenAndChatId(getInitialInstantFrom(secretionRankPeriod), getFinalInstantFrom(secretionRankPeriod), chatId);
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

    private Instant getInitialInstantFrom(SecretionRankPeriod secretionRankPeriod) {
        if (secretionRankPeriod == SecretionRankPeriod.PAST_WEEK) {
            LocalDate pastWeekFirstDay = LocalDate.now(clock).minusWeeks(1L).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            return pastWeekFirstDay.atStartOfDay(ZONE_ID).toInstant();
        }

        LocalDate pastMonthFirstDay = LocalDate.now(clock).minusMonths(1);
        return LocalDate.of(pastMonthFirstDay.getYear(), pastMonthFirstDay.getMonthValue(), 1).atStartOfDay(ZONE_ID).toInstant();
    }

    private Instant getFinalInstantFrom(SecretionRankPeriod secretionRankPeriod) {
        if (secretionRankPeriod == SecretionRankPeriod.PAST_WEEK) {
            LocalDate pastWeekLastDay = LocalDate.now(clock).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            return pastWeekLastDay.atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
        }

        LocalDate pastMonthLastDate = LocalDate.now(clock).minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        return pastMonthLastDate.atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
    }
}
