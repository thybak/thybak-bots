package com.thybak.bots.kkbot;

import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KkBotTest {

    private KkBot kkBot;

    private List<KkBotAction> kkBotActions;

    @BeforeEach
    public void setup() {
        kkBotActions = new ArrayList<>();
        kkBot = new KkBot(null, kkBotActions);
    }

    @Test
    void givenAnExistentUpdateWithAction_whenOnUpdateReceived_thenActionIsExecuted() {
        kkBotActions.clear();
        KkBotAction mockedAction = TestHelper.givenMockedAction();
        kkBotActions.add(mockedAction);

        Update update = TestHelper.givenUpdateAction();
        kkBot.onUpdateReceived(update);

        Mockito.verify(mockedAction).executeAction(update);
    }

    @Test
    void givenAMissingUpdateAction_whenOnUpdateReceived_thenNoActionIsExecuted() {
        kkBotActions.clear();
        KkBotAction mockedAction = TestHelper.givenMockedAction();
        kkBotActions.add(mockedAction);

        Update update = TestHelper.givenNotFoundAction();
        kkBot.onUpdateReceived(update);

        Mockito.verify(mockedAction, Mockito.times(0)).executeAction(update);
    }

    @Test
    void givenANotActionText_whenOnUpdateReceived_thenDoNothing() {
        kkBotActions.clear();
        KkBotAction mockedAction = TestHelper.givenMockedAction();
        kkBotActions.add(mockedAction);

        Update update = TestHelper.givenNoActionUpdate();
        kkBot.onUpdateReceived(update);

        Mockito.verify(mockedAction, Mockito.times(0)).executeAction(update);
    }


    private final static class TestHelper {
        private static final String COMMAND = "COMMAND";
        private static final Long CHAT_ID = 1L;

        private static Update givenUpdateAction() {
            Update update = createUpdate();
            update.getMessage().setText(COMMAND);

            return update;
        }

        private static Update givenNotFoundAction() {
            Update update = createUpdate();
            update.getMessage().setText("/notFoundAction");

            return update;
        }

        private static Update givenNoActionUpdate() {
            Update update = createUpdate();
            update.getMessage().setText("NO_ACTION");

            return update;
        }

        private static Update createUpdate() {
            Message message = new Message();
            Chat chat = new Chat();
            chat.setId(CHAT_ID);
            message.setText("NO_ACTION");
            message.setChat(chat);
            Update update = new Update();
            update.setMessage(message);

            return update;
        }

        private static KkBotAction givenMockedAction() {
            KkBotAction kkBotAction = Mockito.mock(KkBotAction.class);
            Mockito.when(kkBotAction.getCommand()).thenReturn(COMMAND);
            return kkBotAction;
        }
    }
}