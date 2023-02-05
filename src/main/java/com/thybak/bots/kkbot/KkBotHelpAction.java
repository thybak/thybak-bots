package com.thybak.bots.kkbot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class KkBotHelpAction implements KkBotAction {

    @Override
    public String getCommand() {
        return Constants.HELP_COMMAND;
    }

    @Override
    public SendMessage executeAction(Update update) {
        SendMessage response = createResponseMessageSenderFrom(update);
        response.setText(Constants.HELP_COMMAND_TEXT);
        return response;
    }

    private static final class Constants {
        private static final String HELP_COMMAND = "/help";
        private static final String HELP_COMMAND_TEXT = "Hola! Soy tu registrador de kks. Encantado!\n" +
                "Lo primero de todo, descuida! Los datos solo me interesan a mí y solo guardo las fechas en que registras que sacó la cabeza la tortuga. \n\n" +
                "De momento *éstas son mis funcionalidades*:\n\n" +
                "*Almacenar que hemos pasado por el cagadero*: Escribe simplemente un emoji de una kk y te confirmaré que he recibido tu mensaje cada vez. \n" +
                "__En construcción__: permitir sacar de manera individual estadísticas diarias, mensuales y anuales // preparar un ranking diario, mensual y anual";
    }
}
