package com.thybak.bots.kkbot.adapter.inbound;

import com.thybak.bots.kkbot.adapter.inbound.action.KkBotAction;
import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

class KkBotActionHandlerTest {

    private KkBotActionHandler kkBotActionHandler;

    private List<KkBotAction> kkBotActions;

    @BeforeEach
    public void setup() {
        kkBotActions = new ArrayList<>();
        kkBotActionHandler = new KkBotActionHandler(null, kkBotActions);
    }

    @Test
    void givenAnExistentUpdateWithAction_whenOnUpdateReceived_thenActionIsExecuted() {
        kkBotActions.clear();
        KkBotAction mockedAction = TestHelper.givenMockedAction();
        kkBotActions.add(mockedAction);

        Update update = TestHelper.givenUpdateAction();
        kkBotActionHandler.onUpdateReceived(update);

        Mockito.verify(mockedAction).executeAction(update);
    }

    @Test
    void givenAMissingUpdateAction_whenOnUpdateReceived_thenNoActionIsExecuted() {
        kkBotActions.clear();
        KkBotAction mockedAction = TestHelper.givenMockedAction();
        kkBotActions.add(mockedAction);

        Update update = TestHelper.givenNotFoundAction();
        kkBotActionHandler.onUpdateReceived(update);

        Mockito.verify(mockedAction, Mockito.times(0)).executeAction(update);
    }

    @Test
    void givenANotActionText_whenOnUpdateReceived_thenDoNothing() {
        kkBotActions.clear();
        KkBotAction mockedAction = TestHelper.givenMockedAction();
        kkBotActions.add(mockedAction);

        Update update = TestHelper.givenNoActionUpdate();
        kkBotActionHandler.onUpdateReceived(update);

        Mockito.verify(mockedAction, Mockito.times(0)).executeAction(update);
    }


    private final static class TestHelper {
        private static final String COMMAND = "COMMAND";
        private static final String EXECUTION_RESPONSE = "EXECUTION_RESPONSE";
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
            Mockito.when(kkBotAction.executeAction(ArgumentMatchers.any(Update.class))).thenReturn(ActionResponse.builder().text(EXECUTION_RESPONSE).build());
            return kkBotAction;
        }
    }
}