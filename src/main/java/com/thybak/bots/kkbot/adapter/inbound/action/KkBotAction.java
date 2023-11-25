package com.thybak.bots.kkbot.adapter.inbound.action;

import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface KkBotAction {

    String getCommand();
    ActionResponse executeAction(Update update);

}
