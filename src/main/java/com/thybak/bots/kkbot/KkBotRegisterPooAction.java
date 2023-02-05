package com.thybak.bots.kkbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class KkBotRegisterPooAction implements KkBotAction {
    private final KkBotService kkBotService;
    private final Random random = new Random();

    @Override
    public String getCommand() {
        return Constants.POO_COMMAND;
    }

    @Override
    public SendMessage executeAction(Update update) {
        SendMessage response = createResponseMessageSenderFrom(update);
        if (kkBotService.registerPooFrom(update)) {
            response.setText(getRandomSuccessfulPooRegisterMessage());
        } else {
            response.setText(Constants.SAVE_ERROR_MESSAGE);
        }
        return response;
    }

    private String getRandomSuccessfulPooRegisterMessage() {
        return Constants.SUCCESS_POO_REGISTER_MESSAGES[this.random.nextInt(Constants.SUCCESS_POO_REGISTER_MESSAGES.length - 1)];
    }

    private static final class Constants {
        private static final String[] SUCCESS_POO_REGISTER_MESSAGES = new String[]{"Tremendo trocolo, espero que te hayas limpiado bien!", "No habrás echado solo una bolita, eh?", "Vaaaamos niño!", "Uffff, creías que este momento nunca llegaría eh?", "OK. Y a mí qué me cuentas?", "Te pagan por cagar o qué?", "Lol o k", "Oh vaya, ten cuidado no te tengan que dar puntos jej", "Caga el cura, caga el papa, de cagar nadie se escapa", "Cagar por la mañana y abundante alarga la vida de cualquier tunante", "Sin duda, de los placeres sin pecar, el más barato es el cagar", "Este man tenía a Jordan colgando del aro"};
        private static final String SAVE_ERROR_MESSAGE = "Mi má, atascaste las tuberías y tu kk no se ha podido guardar. Habla con el Siva o con el Quimi para que limpien tu mierda";
        private static final String POO_COMMAND = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0xa9});
    }
}
