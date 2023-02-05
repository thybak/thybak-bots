package com.thybak.bots.kkbot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KkBotServiceTest {

    @InjectMocks
    private KkBotService kkBotService;

    @Mock
    private PooRepository pooRepository;

    @Test
    void givenAValidUpdate_whenRegisterPooFrom_thenReturnTrue() {
        Update update = TestHelper.givenValidUpdate();

        Mockito.when(pooRepository.save(ArgumentMatchers.any(Poo.class))).thenReturn(TestHelper.createExpectedPoo());

        assertTrue(kkBotService.registerPooFrom(update));

        Mockito.verify(pooRepository).save(ArgumentMatchers.any(Poo.class));
    }

    @Test
    void givenAnInvalidUpdateWithInvalidMessage_whenRegisterPooFrom_thenReturnFalse() {
        Update update = TestHelper.givenInvalidUpdateWithInvalidMessage();

        assertFalse(kkBotService.registerPooFrom(update));
    }

    @Test
    void givenAnInvalidUpdateWithoutAuthor_whenRegisterPooFrom_thenReturnFalse() {
        Update update = TestHelper.givenInvalidUpdateWithoutAuthor();

        assertFalse(kkBotService.registerPooFrom(update));
    }

    @Test
    void givenAnInvalidUpdateWithoutChatId_whenRegisterPooFrom_thenReturnFalse() {
        Update update = TestHelper.givenInvalidUpdateWithoutChatId();

        assertFalse(kkBotService.registerPooFrom(update));
    }

    @Test
    void givenARepositoryExceptionWhenSaving_whenRegisterPooFrom_thenReturnFalse() {
        Update update = TestHelper.givenValidUpdate();

        Mockito.when(pooRepository.save(ArgumentMatchers.any(Poo.class))).thenThrow(new RuntimeException());

        assertFalse(kkBotService.registerPooFrom(update));

        Mockito.verify(pooRepository).save(ArgumentMatchers.any(Poo.class));
    }



    private static final class TestHelper {
        private TestHelper() {
        }

        private static final String POO_MESSAGE = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0xa9});
        private static final String RANDOM_MESSAGE = "RANDOM_MESSAGE";
        private static final String USER_NAME = "USER_NAME";
        private static final Long CHAT_ID = 1L;

        private static Update givenValidUpdate() {
            Update update = createUpdateFrom(TestHelper.POO_MESSAGE);
            update.getMessage().setFrom(createFromUser());
            update.getMessage().setChat(createChat());

            return update;
        }

        private static Update givenInvalidUpdateWithInvalidMessage() {
            return createUpdateFrom(TestHelper.RANDOM_MESSAGE);
        }

        private static Update givenInvalidUpdateWithoutAuthor() {
            Update update = createUpdateFrom(TestHelper.POO_MESSAGE);
            return update;
        }

        private static Update givenInvalidUpdateWithoutChatId () {
            Update update = createUpdateFrom(TestHelper.POO_MESSAGE);
            update.getMessage().setFrom(createFromUser());
            update.getMessage().setChat(new Chat());

            return update;
        }

        private static Update createUpdateFrom(String text) {
            Update update = new Update();
            Message message = new Message();
            message.setText(text);
            update.setMessage(message);

            return update;
        }

        private static User createFromUser() {
            User user = new User();
            user.setUserName(TestHelper.USER_NAME);
            return user;
        }

        private static Chat createChat() {
            Chat chat = new Chat();
            chat.setId(TestHelper.CHAT_ID);
            return chat;
        }

        private static Poo createExpectedPoo() {
            return new Poo(TestHelper.USER_NAME, Instant.now(), TestHelper.CHAT_ID);
        }
    }
}