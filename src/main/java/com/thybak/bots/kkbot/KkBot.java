package com.thybak.bots.kkbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KkBot extends TelegramLongPollingBot {
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
        if (update.getMessage() == null)
            return;

        System.out.printf("Mensaje leído: %s%n", update);
        String updateText = update.getMessage().getText();

        Optional<KkBotAction> kkBotAction = kkBotActions.stream().filter(kkBotActionItem -> kkBotActionItem.getCommand().equals(updateText)).findFirst();

        SendMessage responseMessageSender;
        if (kkBotAction.isEmpty()) {
            Long chatId = update.getMessage().getChatId();
            responseMessageSender = new SendMessage();
            responseMessageSender.setChatId(chatId);
            responseMessageSender.setText(Constants.ACTION_NOT_FOUND);
        } else {
            responseMessageSender = kkBotAction.get().executeAction(update);
        }

        try {
            execute(responseMessageSender);
        } catch (Exception ex) {
            System.out.printf("Oops! Error al mandar el mensaje. Más info: %s%n", ex);
        }
    }

    private static final class Constants {
        private static final String ACTION_NOT_FOUND = "Que lo qué? No entiendo lo que me dices, te oigo desde el WC";

    }


}
