package com.thybak.bots.kkbot.adapter.inbound;

import com.thybak.bots.kkbot.config.KkBotConfigurationProperties;
import com.thybak.bots.kkbot.domain.model.PooRankPeriod;
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

    private final KkBotActionHandler kkBotActionHandler;
    private final KkBotConfigurationProperties kkBotConfigurationProperties;

    @Scheduled(cron = EVERY_MONDAY_AT_8_AM, zone = SPAIN_LOCAL_ZONE)
    public void publishWeeklyRank() {
        final Update weeklyRankAction = createRankUpdateAction(PooRankPeriod.PAST_WEEK);
        kkBotActionHandler.onUpdateReceived(weeklyRankAction);
    }

    @Scheduled(cron = EVERY_FIRST_DAY_OF_MONTH_AT_8_AM, zone = SPAIN_LOCAL_ZONE)
    public void publishMonthlyRank() {
        final Update monthlyRankAction = createRankUpdateAction(PooRankPeriod.PAST_MONTH);
        kkBotActionHandler.onUpdateReceived(monthlyRankAction);
    }

    private Update createRankUpdateAction(PooRankPeriod pooRankPeriod)
    {
        final Update rankUpdateAction = new Update();
        final Message message = new Message();
        message.setChat(new Chat(kkBotConfigurationProperties.getGroupChatId(), ""));
        message.setText(String.format("/rank %s", pooRankPeriod.getPeriodName()));
        rankUpdateAction.setMessage(message);

        return rankUpdateAction;
    }
}
