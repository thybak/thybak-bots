package com.thybak.bots.kkbot.action;

import com.thybak.bots.kkbot.domain.ActionResponse;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface KkBotAction {

    String getCommand();
    ActionResponse executeAction(Update update);

}
