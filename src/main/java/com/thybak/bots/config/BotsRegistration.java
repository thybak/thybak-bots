package com.thybak.bots.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Configuration
public class BotsRegistration {
    @Autowired
    public BotsRegistration(List<TelegramLongPollingBot> bots) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            for (TelegramLongPollingBot bot : bots) {
                botsApi.registerBot(bot);
            }
        } catch (TelegramApiException tae) {
            tae.printStackTrace();
        }
    }
}
