package com.thybak.bots.kkbot.domain.usecase;

import com.thybak.bots.kkbot.domain.model.Secretion;
import com.thybak.bots.kkbot.domain.model.PooRankEntry;
import com.thybak.bots.kkbot.domain.model.PooRankPeriod;
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

    public List<PooRankEntry> run(PooRankPeriod pooRankPeriod, Long chatId) {
        logger.info("Cálculo de ranking teniendo en cuenta el siguiente periodo comprendido entre {} - {}", getInitialInstantFrom(pooRankPeriod), getFinalInstantFrom(pooRankPeriod));
        final List<Secretion> secretions = secretionRepository.findAllByTimestampBetweenAndChatId(getInitialInstantFrom(pooRankPeriod), getFinalInstantFrom(pooRankPeriod), chatId);

        final Map<String, Long> rankingUnsorted = secretions.stream()
                .filter(secretion -> secretion.getSecretionType().equals(SecretionType.POO))
                .collect(Collectors.groupingBy(Secretion::getUsername, Collectors.counting()));

        final List<PooRankEntry> rankEntries = new ArrayList<>();
        rankingUnsorted.forEach((String username, Long numberOfPoos) -> rankEntries.add(new PooRankEntry(username, numberOfPoos)));

        rankEntries.sort((rankEntry1, rankEntry2) -> rankEntry2.getPoos().compareTo(rankEntry1.getPoos()));

        return rankEntries;
    }


    private Instant getInitialInstantFrom(PooRankPeriod pooRankPeriod) {
        if (pooRankPeriod == PooRankPeriod.PAST_WEEK) {
            LocalDate pastWeekFirstDay = LocalDate.now(clock).minusWeeks(1L).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            return pastWeekFirstDay.atStartOfDay(ZONE_ID).toInstant();
        }

        LocalDate pastMonthFirstDay = LocalDate.now(clock).minusMonths(1);
        return LocalDate.of(pastMonthFirstDay.getYear(), pastMonthFirstDay.getMonthValue(), 1).atStartOfDay(ZONE_ID).toInstant();
    }

    private Instant getFinalInstantFrom(PooRankPeriod pooRankPeriod) {
        if (pooRankPeriod == PooRankPeriod.PAST_WEEK) {
            LocalDate pastWeekLastDay = LocalDate.now(clock).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            return pastWeekLastDay.atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
        }

        LocalDate pastMonthLastDate = LocalDate.now(clock).minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        return pastMonthLastDate.atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
    }
}
