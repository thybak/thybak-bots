package com.thybak.bots.kkbot;

import com.thybak.bots.kkbot.domain.PooRankPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class KkBotScheduler {
    private static final String EVERY_MONDAY_AT_8_AM = "0 0 8 * * 1";
    private static final String EVERY_FIRST_DAY_OF_MONTH_AT_8_AM = "0 0 8 1 * *";
    private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";

    private final KkBot kkBot;
    private final KkBotConfiguration kkBotConfiguration;

    @Scheduled(cron = EVERY_MONDAY_AT_8_AM, zone = SPAIN_LOCAL_ZONE)
    public void publishWeeklyRank() {
        Update weeklyRankAction = createRankUpdateAction(PooRankPeriod.PAST_WEEK);
        kkBot.onUpdateReceived(weeklyRankAction);
    }

    @Scheduled(cron = EVERY_FIRST_DAY_OF_MONTH_AT_8_AM, zone = SPAIN_LOCAL_ZONE)
    public void publishMonthlyRank() {
        Update monthlyRankAction = createRankUpdateAction(PooRankPeriod.PAST_MONTH);
        kkBot.onUpdateReceived(monthlyRankAction);
    }

    private Update createRankUpdateAction(PooRankPeriod pooRankPeriod)
    {
        Update rankUpdateAction = new Update();
        Message message = new Message();
        message.setChat(new Chat(kkBotConfiguration.getGroupChatId(), ""));
        message.setText(String.format("/rank %s", pooRankPeriod.getPeriodName()));
        rankUpdateAction.setMessage(message);

        return rankUpdateAction;
    }
}
