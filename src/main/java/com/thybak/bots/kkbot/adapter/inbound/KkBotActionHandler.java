package com.thybak.bots.kkbot.adapter.inbound;

import com.thybak.bots.kkbot.config.KkBotConfigurationProperties;
import com.thybak.bots.kkbot.adapter.inbound.action.KkBotAction;
import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KkBotActionHandler extends TelegramLongPollingBot {
    private static final String ACTION_NOT_FOUND = "Que lo qué? No entiendo lo que me dices, te oigo desde el WC";
    private static final String ERROR_WHEN_SENDING_MESSAGE = "Oops! Error al mandar el mensaje.";

    private final Logger logger = LoggerFactory.getLogger(KkBotActionHandler.class);
    private final KkBotConfigurationProperties kkBotConfigurationProperties;
    private final List<KkBotAction> kkBotActions;

    @Override
    public String getBotUsername() {
        return kkBotConfigurationProperties.getUsername();
    }

    @Override
    public String getBotToken() {
        return kkBotConfigurationProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        final ActionResponse actionResponse = executeBotActionFrom(update);
        if (actionResponse == null || !StringUtils.hasLength(actionResponse.getText()))
            return;

        final SendMessage messageSender = SendMessage.builder().chatId(update.getMessage().getChatId()).parseMode(ParseMode.MARKDOWN).text(actionResponse.getText()).build();
        try {
            execute(messageSender);
        } catch (Exception ex) {
            logger.error(ERROR_WHEN_SENDING_MESSAGE, ex);
        }
    }

    private ActionResponse executeBotActionFrom(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return null;

        final String updateText = update.getMessage().getText().split(" ")[0];
        final Optional<KkBotAction> kkBotAction = kkBotActions.stream().filter(kkBotActionItem -> kkBotActionItem.getCommand().equals(updateText)).findFirst();

        if (kkBotAction.isEmpty() && !updateText.startsWith("/"))
            return null;

        ActionResponse actionResponse;
        if (kkBotAction.isEmpty()) {
            actionResponse = ActionResponse.builder().text(ACTION_NOT_FOUND).build();
        } else {
            actionResponse = kkBotAction.get().executeAction(update);
        }

        return actionResponse;
    }
}
