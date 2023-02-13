package com.thybak.bots.kkbot;

import com.thybak.bots.kkbot.domain.Poo;
import com.thybak.bots.kkbot.domain.PooRankEntry;
import com.thybak.bots.kkbot.domain.PooRankPeriod;
import com.thybak.bots.kkbot.domain.PooRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KkBotService {
    private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";
    private static final ZoneId ZONE_ID = ZoneId.of(SPAIN_LOCAL_ZONE);

    private final Logger logger = LoggerFactory.getLogger(KkBotService.class);
    private final Clock clock;
    private final PooRepository pooRepository;

    public boolean registerPooFrom(Update update) {
        Message message = update.getMessage();
        if (!hasAuthorUsername(message)) {
            logger.error("El autor de la kk no tiene username registrado {}", update);
            return false;
        }
        if (message.getChatId() == null) {
            logger.error("El mensaje no tiene ningún chatId asociado, peligro! {}", update);
            return false;
        }

        Poo poo = new Poo(message.getFrom().getUserName(), Instant.now(), message.getChatId());
        try {
            logger.info("KK satisfactoriamente registrada {}%n", pooRepository.save(poo));
            return true;
        } catch (Exception exception) {
            logger.error("Hubo un error almacenando en la base de datos el siguiente elemento -> {}", poo);
            return false;
        }
    }

    public List<PooRankEntry> getPooRankingFrom(PooRankPeriod pooRankPeriod, Long chatId) {
        logger.info("Cálculo de ranking teniendo en cuenta el siguiente periodo comprendido entre {} - {}", getInitialInstantFrom(pooRankPeriod), getFinalInstantFrom(pooRankPeriod));
        List<Poo> poos = pooRepository.findAllByTimestampBetweenAndChatId(getInitialInstantFrom(pooRankPeriod), getFinalInstantFrom(pooRankPeriod), chatId);

        Map<String, Long> rankingUnsorted = poos.stream().collect(Collectors.groupingBy(Poo::getUsername, Collectors.counting()));

        List<PooRankEntry> rankEntries = new ArrayList<>();
        rankingUnsorted.forEach((String username, Long numberOfPoos) -> rankEntries.add(new PooRankEntry(username, numberOfPoos)));

        rankEntries.sort((rankEntry1, rankEntry2) -> rankEntry2.getPoos().compareTo(rankEntry1.getPoos()));

        return rankEntries;
    }

    private boolean hasAuthorUsername(Message message) {
        return message.getFrom() != null && message.getFrom().getUserName() != null;
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
