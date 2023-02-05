package com.thybak.bots.kkbot;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface KkBotAction {
    default SendMessage createResponseMessageSenderFrom(Update update) {
        final long chatId = update.getMessage().getChatId();
        SendMessage responseMessageSender = new SendMessage();
        responseMessageSender.setChatId(chatId);
        responseMessageSender.setParseMode(ParseMode.MARKDOWN);

        return responseMessageSender;
    }
    String getCommand();
    SendMessage executeAction(Update update);

}
