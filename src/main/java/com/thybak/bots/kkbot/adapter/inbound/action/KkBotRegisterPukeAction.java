package com.thybak.bots.kkbot.adapter.inbound.action;

import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import com.thybak.bots.kkbot.domain.model.SecretionType;
import com.thybak.bots.kkbot.domain.usecase.RegisterSecretionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class KkBotRegisterPukeAction implements KkBotAction {
    private static final String SAVE_ERROR_MESSAGE = "Mi má, atascaste las tuberías y tu pota no se ha podido guardar. Habla con alguien pa que limpien tu mierda";
    private static final String PUKE_COMMAND = "\uD83E\uDD2E";

    private final RegisterSecretionUseCase registerSecretionUseCase;

    @Override
    public String getCommand() {
        return PUKE_COMMAND;
    }

    @Override
    public ActionResponse executeAction(Update update) {
        final ActionResponse actionResponse = ActionResponse.builder().build();
        if (!registerSecretionUseCase.run(update, SecretionType.PUKE)) {
            actionResponse.setText(SAVE_ERROR_MESSAGE);
        }
        return actionResponse;
    }
}
