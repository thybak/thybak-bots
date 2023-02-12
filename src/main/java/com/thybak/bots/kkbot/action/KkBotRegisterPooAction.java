package com.thybak.bots.kkbot.action;

import com.thybak.bots.kkbot.KkBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class KkBotRegisterPooAction implements KkBotAction {
    private static final String SAVE_ERROR_MESSAGE = "Mi má, atascaste las tuberías y tu kk no se ha podido guardar. Habla con el Siva o con el Quimi para que limpien tu mierda";
    private static final String POO_COMMAND = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0xa9});

    private final KkBotService kkBotService;

    @Override
    public String getCommand() {
        return POO_COMMAND;
    }

    @Override
    public SendMessage executeAction(Update update) {
        SendMessage response = createResponseMessageSenderFrom(update);
        if (!kkBotService.registerPooFrom(update)) {
            response.setText(SAVE_ERROR_MESSAGE);
        }
        return response;
    }
}
