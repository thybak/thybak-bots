package com.thybak.bots.kkbot.action;

import com.thybak.bots.kkbot.domain.ActionResponse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class KkBotHelpAction implements KkBotAction {
    private static final String HELP_COMMAND = "/help";
    private static final String HELP_COMMAND_TEXT = """
            Hola! Soy tu registrador de kks. Encantado!
            Lo primero de todo, descuida! Los datos solo me interesan a mí y solo guardo las fechas en que registras que sacó la cabeza la tortuga.
            
            De momento *éstas son mis funcionalidades*:
            
            *Almacenar que hemos pasado por el cagadero*: Escribe simplemente un emoji de una kk y en caso de no poder confirmar el registro te avisaré.
            *Ranking de kks*: cada lunes y cada día 1 de cada mes calcularé el ranking de kks a las 8 de la mañana para vosotros!""";

    @Override
    public String getCommand() {
        return HELP_COMMAND;
    }

    @Override
    public ActionResponse executeAction(Update update) {
        return ActionResponse.builder().text(HELP_COMMAND_TEXT).build();
    }
}
