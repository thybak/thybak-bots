package com.thybak.bots.kkbot.adapter.inbound.action;

import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class KkBotHelpActionTest {

    @InjectMocks
    private KkBotHelpAction kkBotHelpAction;

    @Test
    void whenGetCommand_thenReturnHelpCommand() {
        assertEquals(TestHelper.HELP_COMMAND, kkBotHelpAction.getCommand());
    }

    @Test
    void whenExecuteAction_thenReturnHelpCommandMessage() {
        ActionResponse actionResponse = kkBotHelpAction.executeAction(TestHelper.givenHelpUpdate());
        assertEquals(TestHelper.HELP_COMMAND_TEXT, actionResponse.getText());
    }

    private static final class TestHelper {
        private static final String HELP_COMMAND = "/help";
        private static final String HELP_COMMAND_TEXT = """
            Hola! Soy tu registrador de kks. Encantado!
            Lo primero de todo, descuida! Los datos solo me interesan a mí y solo guardo las fechas en que registras que sacó la cabeza la tortuga, entre otros.
                        
            De momento *éstas son mis funcionalidades*:
                        
            *Almacenar que hemos pasado por el cagadero*: Escribe simplemente un emoji de una kk y en caso de no poder confirmar el registro te avisaré.
            *Almacenar que hemos gomitao*: Desahógate con nosotros y escribe \uD83E\uDD2E. En caso de no poder confirmar el registro te avisaré.
            *Ranking de secreciones*: cada lunes y cada día 1 de cada mes calcularé el ranking de kks y potas a las 8 de la mañana para vosotros!""";

        private static final long CHAT_ID = 1L;

        private static Update givenHelpUpdate() {
            Update update = createUpdate();
            update.getMessage().setText(HELP_COMMAND);

            return update;
        }

        private static Update createUpdate() {
            Chat chat = new Chat();
            chat.setId(CHAT_ID);
            Message message = new Message();
            message.setChat(chat);
            Update update = new Update();
            update.setMessage(message);

            return update;
        }
    }
}