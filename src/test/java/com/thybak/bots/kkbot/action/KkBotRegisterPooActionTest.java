package com.thybak.bots.kkbot.action;

import com.thybak.bots.kkbot.KkBotService;
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

        assertNull(response.getText());
    }

    @Test
    void givenFailedPooRegistration_whenExecuteAction_thenReturnSaveErrorMessage() {
        Mockito.when(kkBotService.registerPooFrom(ArgumentMatchers.any(Update.class))).thenReturn(false);

        SendMessage response = kkBotRegisterPooAction.executeAction(TestHelper.givenPooUpdate());

        assertEquals(TestHelper.SAVE_ERROR_MESSAGE, response.getText());
    }

    private static final class TestHelper {
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