package com.thybak.bots.kkbot;

import com.thybak.bots.kkbot.domain.Poo;
import com.thybak.bots.kkbot.domain.PooRankEntry;
import com.thybak.bots.kkbot.domain.PooRankPeriod;
import com.thybak.bots.kkbot.domain.PooRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KkBotServiceTest {

    @InjectMocks
    private KkBotService kkBotService;

    @Mock
    private PooRepository pooRepository;

    @Mock
    private Clock clock;

    @Test
    void givenAValidUpdate_whenRegisterPooFrom_thenReturnTrue() {
        Update update = TestHelper.givenValidUpdate();

        Mockito.when(pooRepository.save(ArgumentMatchers.any(Poo.class))).thenReturn(TestHelper.createExpectedPoo());

        assertTrue(kkBotService.registerPooFrom(update));

        Mockito.verify(pooRepository).save(ArgumentMatchers.any(Poo.class));
    }

    @Test
    void givenAnInvalidUpdateWithoutAuthor_whenRegisterPooFrom_thenReturnFalse() {
        Update update = TestHelper.givenInvalidUpdateWithoutAuthor();

        assertFalse(kkBotService.registerPooFrom(update));
    }

    @Test
    void givenAnInvalidUpdateWithoutChatId_whenRegisterPooFrom_thenReturnFalse() {
        Update update = TestHelper.givenInvalidUpdateWithoutChatId();

        assertFalse(kkBotService.registerPooFrom(update));
    }

    @Test
    void givenARepositoryExceptionWhenSaving_whenRegisterPooFrom_thenReturnFalse() {
        Update update = TestHelper.givenValidUpdate();

        Mockito.when(pooRepository.save(ArgumentMatchers.any(Poo.class))).thenThrow(new RuntimeException());

        assertFalse(kkBotService.registerPooFrom(update));

        Mockito.verify(pooRepository).save(ArgumentMatchers.any(Poo.class));
    }

    @Test
    void givenNoPooDataFromAPeriod_whenGetPooRankingFrom_thenReturnEmptyList() {
        givenFixedClock();
        Mockito.when(pooRepository.findAllByTimestampBetweenAndChatId(ArgumentMatchers.any(Instant.class), ArgumentMatchers.any(Instant.class), ArgumentMatchers.anyLong())).thenReturn(List.of());

        assertEquals(0, kkBotService.getPooRankingFrom(PooRankPeriod.PAST_WEEK, TestHelper.CHAT_ID).size());
    }

    @Test
    void givenPooDataFromAWeeklyPeriod_whenGetPooRankingFrom_thenReturnPooRanking() {
        givenFixedClock();
        Mockito.when(pooRepository.findAllByTimestampBetweenAndChatId(TestHelper.INITIAL_WEEK_PERIOD_INSTANT, TestHelper.FINAL_WEEK_PERIOD_INSTANT, TestHelper.CHAT_ID)).thenReturn(TestHelper.POO_WEEK_ENTRIES);

        List<PooRankEntry> pooRanking = kkBotService.getPooRankingFrom(PooRankPeriod.PAST_WEEK, TestHelper.CHAT_ID);

        assertArrayEquals(TestHelper.EXPECTED_WEEK_POO_RANKING.toArray(), pooRanking.toArray());
    }

    @Test
    void givenPooDataFromAMonthlyPeriod_whenGetPooRankingFrom_thenReturnPooRanking() {
        givenFixedClock();
        Mockito.when(pooRepository.findAllByTimestampBetweenAndChatId(TestHelper.INITIAL_MONTH_PERIOD_INSTANT, TestHelper.FINAL_MONTH_PERIOD_INSTANT, TestHelper.CHAT_ID)).thenReturn(TestHelper.POO_MONTH_ENTRIES);

        List<PooRankEntry> pooRanking = kkBotService.getPooRankingFrom(PooRankPeriod.PAST_MONTH, TestHelper.CHAT_ID);

        assertArrayEquals(TestHelper.EXPECTED_MONTH_POO_RANKING.toArray(), pooRanking.toArray());
    }

    private void givenFixedClock() {
        Clock fixedClock = Clock.fixed(TestHelper.LOCAL_DATE.atStartOfDay(TestHelper.ZONE_ID).toInstant(), TestHelper.ZONE_ID);
        Mockito.when(clock.instant()).thenReturn(fixedClock.instant());
        Mockito.when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    private static final class TestHelper {
        private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";
        private static final ZoneId ZONE_ID = ZoneId.of(SPAIN_LOCAL_ZONE);
        private static final String POO_MESSAGE = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0xa9});
        private static final String USER_NAME = "USER_NAME";
        private static final Long CHAT_ID = 1L;
        private static final LocalDate LOCAL_DATE = LocalDate.of(2023, 2, 12);
        private static final Instant INITIAL_WEEK_PERIOD_INSTANT = Instant.parse("2023-01-29T23:00:00Z");
        private static final Instant FINAL_WEEK_PERIOD_INSTANT = Instant.parse("2023-02-05T22:59:59.999999999Z");
        private static final Instant INITIAL_MONTH_PERIOD_INSTANT = Instant.parse("2022-12-31T23:00:00Z");
        private static final Instant FINAL_MONTH_PERIOD_INSTANT = Instant.parse("2023-01-31T22:59:59.999999999Z");

        private static final List<Poo> POO_WEEK_ENTRIES = List.of(
                new Poo("USERNAME1", INITIAL_WEEK_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME1", INITIAL_WEEK_PERIOD_INSTANT.plus(3L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME1", INITIAL_WEEK_PERIOD_INSTANT.plus(4L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME2", INITIAL_WEEK_PERIOD_INSTANT.plus(1L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME2", INITIAL_WEEK_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID)
        );
        private static final List<PooRankEntry> EXPECTED_WEEK_POO_RANKING = List.of(
                new PooRankEntry("USERNAME1", 3L),
                new PooRankEntry("USERNAME2", 2L)
        );
        private static final List<Poo> POO_MONTH_ENTRIES = List.of(
                new Poo("USERNAME1", INITIAL_MONTH_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME1", INITIAL_MONTH_PERIOD_INSTANT.plus(3L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME1", INITIAL_MONTH_PERIOD_INSTANT.plus(4L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME2", INITIAL_MONTH_PERIOD_INSTANT.plus(1L, ChronoUnit.DAYS), CHAT_ID),
                new Poo("USERNAME2", INITIAL_MONTH_PERIOD_INSTANT.plus(2L, ChronoUnit.DAYS), CHAT_ID)
        );
        private static final List<PooRankEntry> EXPECTED_MONTH_POO_RANKING = List.of(
                new PooRankEntry("USERNAME1", 3L),
                new PooRankEntry("USERNAME2", 2L)
        );

        private static Update givenValidUpdate() {
            Update update = createUpdateFrom();
            update.getMessage().setFrom(createFromUser());
            update.getMessage().setChat(createChat());

            return update;
        }

        private static Update givenInvalidUpdateWithoutAuthor() {
            return createUpdateFrom();
        }

        private static Update givenInvalidUpdateWithoutChatId() {
            Update update = createUpdateFrom();
            update.getMessage().setFrom(createFromUser());
            update.getMessage().setChat(new Chat());

            return update;
        }

        private static Update createUpdateFrom() {
            Update update = new Update();
            Message message = new Message();
            message.setText(TestHelper.POO_MESSAGE);
            update.setMessage(message);

            return update;
        }

        private static User createFromUser() {
            User user = new User();
            user.setUserName(TestHelper.USER_NAME);
            return user;
        }

        private static Chat createChat() {
            Chat chat = new Chat();
            chat.setId(TestHelper.CHAT_ID);
            return chat;
        }

        private static Poo createExpectedPoo() {
            return new Poo(TestHelper.USER_NAME, Instant.now(), TestHelper.CHAT_ID);
        }
    }
}