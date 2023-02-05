package com.thybak.bots.kkbot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KkBotRegisterPooActionTest {

    @InjectMocks
    private KkBotRegisterPooAction kkBotRegisterPooAction;

    @Mock
    private KkBotService kkBotService;

    @Test
    void whenGetCommand_thenReturnAPoo() {
        assertEquals(TestHelper.POO_COMMAND, kkBotRegisterPooAction.getCommand());
    }

    @Test
    void givenSuccessfulPooRegistration_whenExecuteAction_thenReturnRandomSuccessfulMessage() {
        Mockito.when(kkBotService.registerPooFrom(ArgumentMatchers.any(Update.class))).thenReturn(true);

        SendMessage response = kkBotRegisterPooAction.executeAction(TestHelper.givenPooUpdate());

        assertTrue(Arrays.stream(TestHelper.SUCCESS_POO_REGISTER_MESSAGES)
                .anyMatch(message -> message.equals(response.getText())));
    }

    @Test
    void givenFailedPooRegistration_whenExecuteAction_thenReturnSaveErrorMessage() {
        Mockito.when(kkBotService.registerPooFrom(ArgumentMatchers.any(Update.class))).thenReturn(false);

        SendMessage response = kkBotRegisterPooAction.executeAction(TestHelper.givenPooUpdate());

        assertEquals(TestHelper.SAVE_ERROR_MESSAGE, response.getText());
    }

    private static final class TestHelper {
        private static final String[] SUCCESS_POO_REGISTER_MESSAGES = new String[]{"Tremendo trocolo, espero que te hayas limpiado bien!", "No habrás echado solo una bolita, eh?", "Vaaaamos niño!", "Uffff, creías que este momento nunca llegaría eh?", "OK. Y a mí qué me cuentas?", "Te pagan por cagar o qué?", "Lol o k", "Oh vaya, ten cuidado no te tengan que dar puntos jej", "Caga el cura, caga el papa, de cagar nadie se escapa", "Cagar por la mañana y abundante alarga la vida de cualquier tunante", "Sin duda, de los placeres sin pecar, el más barato es el cagar", "Este man tenía a Jordan colgando del aro"};
        private static final String POO_COMMAND = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0xa9});
        private static final String SAVE_ERROR_MESSAGE = "Mi má, atascaste las tuberías y tu kk no se ha podido guardar. Habla con el Siva o con el Quimi para que limpien tu mierda";
        private static final long CHAT_ID = 1L;

        private static Update givenPooUpdate() {
            Update update = createUpdate();
            update.getMessage().setText(POO_COMMAND);

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