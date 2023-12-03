package com.thybak.bots.kkbot.adapter.inbound.action;

import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class KkBotHelpAction implements KkBotAction {
    private static final String HELP_COMMAND = "/help";
    private static final String HELP_COMMAND_TEXT = """
            Hola! Soy tu registrador de kks. Encantado!
            Lo primero de todo, descuida! Los datos solo me interesan a mí y solo guardo las fechas en que registras que sacó la cabeza la tortuga, entre otros.
            
            De momento *éstas son mis funcionalidades*:
            
            *Almacenar que hemos pasado por el cagadero*: Escribe simplemente un emoji de una kk y en caso de no poder confirmar el registro te avisaré.
            *Almacenar que hemos gomitao*: Desahógate con nosotros y escribe \uD83E\uDD2E. En caso de no poder confirmar el registro te avisaré.
            *Ranking de secreciones*: cada lunes y cada día 1 de cada mes calcularé el ranking de kks y potas a las 8 de la mañana para vosotros!""";

    @Override
    public String getCommand() {
        return HELP_COMMAND;
    }

    @Override
    public ActionResponse executeAction(Update update) {
        return ActionResponse.builder().text(HELP_COMMAND_TEXT).build();
    }
}
