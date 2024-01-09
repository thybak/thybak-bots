package com.thybak.bots.kkbot.adapter.inbound;

import com.thybak.bots.kkbot.config.KkBotConfigurationProperties;
import com.thybak.bots.kkbot.domain.model.SecretionRankPeriod;
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
    private static final String EVERY_FIRST_DAY_OF_YEAR_AT_12_AM = "0 0 0 1 1 *";
    private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";

    private final KkBotActionHandler kkBotActionHandler;
    private final KkBotConfigurationProperties kkBotConfigurationProperties;

    @Scheduled(cron = EVERY_MONDAY_AT_8_AM, zone = SPAIN_LOCAL_ZONE)
    public void publishWeeklyRank() {
        final Update weeklyRankAction = createRankUpdateAction(SecretionRankPeriod.PAST_WEEK);
        kkBotActionHandler.onUpdateReceived(weeklyRankAction);
    }

    @Scheduled(cron = EVERY_FIRST_DAY_OF_MONTH_AT_8_AM, zone = SPAIN_LOCAL_ZONE)
    public void publishMonthlyRank() {
        final Update monthlyRankAction = createRankUpdateAction(SecretionRankPeriod.PAST_MONTH);
        kkBotActionHandler.onUpdateReceived(monthlyRankAction);
    }

    @Scheduled(cron = EVERY_FIRST_DAY_OF_YEAR_AT_12_AM, zone = SPAIN_LOCAL_ZONE)
    public void publishYearlyRank() {
        final Update yearlyRankAction = createRankUpdateAction(SecretionRankPeriod.PAST_YEAR);
        kkBotActionHandler.onUpdateReceived(yearlyRankAction);
    }

    private Update createRankUpdateAction(SecretionRankPeriod secretionRankPeriod)
    {
        final Update rankUpdateAction = new Update();
        final Message message = new Message();
        message.setChat(new Chat(kkBotConfigurationProperties.getGroupChatId(), ""));
        message.setText(String.format("/rank %s", secretionRankPeriod.getPeriodName()));
        rankUpdateAction.setMessage(message);

        return rankUpdateAction;
    }
}
