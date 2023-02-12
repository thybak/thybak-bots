package com.thybak.bots.kkbot;

import com.thybak.bots.kkbot.action.KkBotAction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KkBot extends TelegramLongPollingBot {
    private static final String ACTION_NOT_FOUND = "Que lo qué? No entiendo lo que me dices, te oigo desde el WC";
    private static final String ERROR_WHEN_SENDING_MESSAGE = "Oops! Error al mandar el mensaje.";

    private final Logger logger = LoggerFactory.getLogger(KkBot.class);
    private final KkBotConfiguration kkBotConfiguration;
    private final List<KkBotAction> kkBotActions;

    @Override
    public String getBotUsername() {
        return kkBotConfiguration.getUsername();
    }

    @Override
    public String getBotToken() {
        return kkBotConfiguration.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        String updateText = update.getMessage().getText().split(" ")[0];
        Optional<KkBotAction> kkBotAction = kkBotActions.stream().filter(kkBotActionItem -> kkBotActionItem.getCommand().equals(updateText)).findFirst();

        if (kkBotAction.isEmpty() && !updateText.startsWith("/"))
            return;

        SendMessage responseMessageSender;
        if (kkBotAction.isEmpty()) {
            Long chatId = update.getMessage().getChatId();
            responseMessageSender = new SendMessage();
            responseMessageSender.setChatId(chatId);
            responseMessageSender.setText(ACTION_NOT_FOUND);
        } else {
            responseMessageSender = kkBotAction.get().executeAction(update);
        }

        try {
            execute(responseMessageSender);
        } catch (Exception ex) {
            logger.error(ERROR_WHEN_SENDING_MESSAGE, ex);
        }
    }
}
