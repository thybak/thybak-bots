package com.thybak.bots.kkbot.domain.usecase;

import com.thybak.bots.kkbot.domain.model.SecretionRankEntry;
import com.thybak.bots.kkbot.domain.model.SecretionRankPeriod;
import com.thybak.bots.kkbot.domain.model.Secretion;
import com.thybak.bots.kkbot.domain.model.SecretionType;
import com.thybak.bots.kkbot.domain.provider.SecretionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GetSecretionRankingUseCaseTest {

    @Mock
    private SecretionRepository secretionRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private GetSecretionRankingUseCase sut;

    @Test
    void givenNoSecretionDataFromAPeriod_whenRun_thenReturnEmptyList() {
        givenFixedClock();
        Mockito.when(secretionRepository.findAllByTimestampBetweenAndChatId(ArgumentMatchers.any(Instant.class), ArgumentMatchers.any(Instant.class), ArgumentMatchers.anyLong())).thenReturn(List.of());

        assertEquals(0, sut.run(SecretionRankPeriod.PAST_WEEK, GetSecretionRankingUseCaseTest.TestHelper.CHAT_ID).size());
    }

    @Test
    void givenSecretionDataFromAWeeklyPeriod_whenRun_thenReturnSecretionRanking() {
        givenFixedClock();
        Mockito.when(secretionRepository.findAllByTimestampBetweenAndChatId(GetSecretionRankingUseCaseTest.TestHelper.INITIAL_WEEK_PERIOD_INSTANT, GetSecretionRankingUseCaseTest.TestHelper.FINAL_WEEK_PERIOD_INSTANT, GetSecretionRankingUseCaseTest.TestHelper.CHAT_ID)).thenReturn(GetSecretionRankingUseCaseTest.TestHelper.SECRETION_WEEK_ENTRIES);

        List<SecretionRankEntry> secretionRanking = sut.run(SecretionRankPeriod.PAST_WEEK, GetSecretionRankingUseCaseTest.TestHelper.CHAT_ID);

        assertArrayEquals(GetSecretionRankingUseCaseTest.TestHelper.EXPECTED_WEEK_SECRETION_RANKING.toArray(), secretionRanking.toArray());
    }

    @Test
    void givenSecretionDataFromAMonthlyPeriod_whenRun_thenReturnSecretionRanking() {
        givenFixedClock();
        Mockito.when(secretionRepository.findAllByTimestampBetweenAndChatId(GetSecretionRankingUseCaseTest.TestHelper.INITIAL_MONTH_PERIOD_INSTANT, GetSecretionRankingUseCaseTest.TestHelper.FINAL_MONTH_PERIOD_INSTANT, GetSecretionRankingUseCaseTest.TestHelper.CHAT_ID)).thenReturn(GetSecretionRankingUseCaseTest.TestHelper.SECRETION_MONTH_ENTRIES);

        List<SecretionRankEntry> secretionRanking = sut.run(SecretionRankPeriod.PAST_MONTH, GetSecretionRankingUseCaseTest.TestHelper.CHAT_ID);

        assertArrayEquals(GetSecretionRankingUseCaseTest.TestHelper.EXPECTED_MONTH_SECRETION_RANKING.toArray(), secretionRanking.toArray());
    }

    private void givenFixedClock() {
        Clock fixedClock = Clock.fixed(GetSecretionRankingUseCaseTest.TestHelper.LOCAL_DATE.atStartOfDay(GetSecretionRankingUseCaseTest.TestHelper.ZONE_ID).toInstant(), GetSecretionRankingUseCaseTest.TestHelper.ZONE_ID);
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    private static final class TestHelper {
        private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";
        private static final ZoneId ZONE_ID = ZoneId.of(SPAIN_LOCAL_ZONE);
        private static final Long CHAT_ID = 1L;
        private static final LocalDate LOCAL_DATE = LocalDate.of(2023, 2, 6);
        private static final Instant INITIAL_WEEK_PERIOD_INSTANT = Instant.parse("2023-01-29T23:00:00Z");
        private static final Instant FINAL_WEEK_PERIOD_INSTANT = Instant.parse("2023-02-05T22:59:59.999999999Z");
        private static final Instant INITIAL_MONTH_PERIOD_INSTANT = Instant.parse("2022-12-31T23:00:00Z");
        private static final Instant FINAL_MONTH_PERIOD_INSTANT = Instant.parse("2023-01-31T22:59:59.999999999Z");

        private static final List<Secretion> SECRETION_WEEK_ENTRIES = List.of(
                new Secretion("USERNAME1", INITIAL_WEEK_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME1", INITIAL_WEEK_PERIOD_INSTANT.plus(3L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME1", INITIAL_WEEK_PERIOD_INSTANT.plus(4L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME2", INITIAL_WEEK_PERIOD_INSTANT.plus(1L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME2", INITIAL_WEEK_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME2", INITIAL_WEEK_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID, SecretionType.PUKE)
        );
        private static final List<SecretionRankEntry> EXPECTED_WEEK_SECRETION_RANKING = List.of(
                new SecretionRankEntry("USERNAME2", 2L, 1L),
                new SecretionRankEntry("USERNAME1", 3L, 0L)
        );
        private static final List<Secretion> SECRETION_MONTH_ENTRIES = List.of(
                new Secretion("USERNAME1", INITIAL_MONTH_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME1", INITIAL_MONTH_PERIOD_INSTANT.plus(3L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME1", INITIAL_MONTH_PERIOD_INSTANT.plus(4L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME2", INITIAL_MONTH_PERIOD_INSTANT.plus(1L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME2", INITIAL_MONTH_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID, SecretionType.POO),
                new Secretion("USERNAME2", INITIAL_MONTH_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID, SecretionType.PUKE)
        );
        private static final List<SecretionRankEntry> EXPECTED_MONTH_SECRETION_RANKING = List.of(
                new SecretionRankEntry("USERNAME2", 2L, 1L),
                new SecretionRankEntry("USERNAME1", 3L, 0L)
        );
    }
}
