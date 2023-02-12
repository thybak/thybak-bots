package com.thybak.bots.kkbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
class KkBotSchedulerTest {

    @InjectMocks
    private KkBotScheduler kkBotScheduler;

    @Mock
    private KkBot kkBot;

    @Mock
    private KkBotConfiguration kkBotConfiguration;

    @BeforeEach
    public void setUp() {
        Mockito.when(kkBotConfiguration.getGroupChatId()).thenReturn(TestHelper.CHAT_ID);
    }

    @Test
    void whenPublishWeeklyRank_thenWeeklyRankActionIsTriggered() {
        kkBotScheduler.publishWeeklyRank();

        Mockito.verify(kkBot).onUpdateReceived(TestHelper.createRankWeekRequest());
    }

    @Test
    void whenPublishMonthlyRank_thenMonthlyRankActionIsTriggered() {
        kkBotScheduler.publishMonthlyRank();

        Mockito.verify(kkBot).onUpdateReceived(TestHelper.createRankMonthRequest());
    }

    private static final class TestHelper {
        private static final Long CHAT_ID = 1L;
        private static final String RANK_COMMAND = "/rank";

        private static Update createRankMonthRequest() {
            Update update = createUpdate();
            update.getMessage().setText(String.format("%s past-month", RANK_COMMAND));

            return update;
        }

        private static Update createRankWeekRequest() {
            Update update = createUpdate();
            update.getMessage().setText(String.format("%s past-week", RANK_COMMAND));

            return update;
        }

        private static Update createUpdate() {
            Update update = new Update();
            Message message = new Message();
            message.setChat(new Chat(CHAT_ID, ""));
            update.setMessage(message);

            return update;
        }


    }
}