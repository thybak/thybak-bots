package com.thybak.bots.kkbot.domain.usecase;

import com.thybak.bots.kkbot.domain.model.Secretion;
import com.thybak.bots.kkbot.domain.model.SecretionType;
import com.thybak.bots.kkbot.domain.provider.SecretionRepository;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RegisterSecretionUseCaseTest {
    @Mock
    private SecretionRepository secretionRepository;
    @InjectMocks
    private RegisterSecretionUseCase sut;

    @Test
    void givenAValidPooUpdate_whenRun_thenReturnTrue() {
        Update update = RegisterSecretionUseCaseTest.TestHelper.givenValidUpdate();

        Mockito.when(secretionRepository.save(ArgumentMatchers.any(Secretion.class))).thenReturn(RegisterSecretionUseCaseTest.TestHelper.createExpectedPoo());

        assertTrue(sut.run(update, SecretionType.POO));

        Mockito.verify(secretionRepository).save(ArgumentMatchers.any(Secretion.class));
    }

    @Test
    void givenAValidPukeUpdate_whenRun_thenReturnTrue() {
        Update update = RegisterSecretionUseCaseTest.TestHelper.givenValidUpdate();

        Mockito.when(secretionRepository.save(ArgumentMatchers.any(Secretion.class))).thenReturn(RegisterSecretionUseCaseTest.TestHelper.createExpectedPuke());

        assertTrue(sut.run(update, SecretionType.PUKE));

        Mockito.verify(secretionRepository).save(ArgumentMatchers.any(Secretion.class));
    }

    @Test
    void givenAnInvalidUpdateWithoutAuthor_whenRun_thenReturnFalse() {
        final Update update = RegisterSecretionUseCaseTest.TestHelper.givenInvalidUpdateWithoutAuthor();

        assertFalse(sut.run(update, SecretionType.POO));
    }
    @Test
    void givenAnInvalidUpdateWithoutChatId_whenRun_thenReturnFalse() {
        final Update update = RegisterSecretionUseCaseTest.TestHelper.givenInvalidUpdateWithoutChatId();

        assertFalse(sut.run(update, SecretionType.POO));
    }

    @Test
    void givenARepositoryExceptionWhenSaving_whenRun_thenReturnFalse() {
        final Update update = RegisterSecretionUseCaseTest.TestHelper.givenValidUpdate();

        Mockito.when(secretionRepository.save(ArgumentMatchers.any(Secretion.class))).thenThrow(new RuntimeException());

        assertFalse(sut.run(update, SecretionType.POO));

        Mockito.verify(secretionRepository).save(ArgumentMatchers.any(Secretion.class));
    }

    private static final class TestHelper {
        private static final String POO_MESSAGE = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0xa9});
        private static final String PUKE_MESSAGE = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0xa4, (byte) 0xae});
        private static final String USER_NAME = "USER_NAME";
        private static final Long CHAT_ID = 1L;

        private static Update givenValidUpdate() {
            Update update = createUpdateFrom();
            update.getMessage().setFrom(createFromUser());
            update.getMessage().setChat(createChat());

            return update;
        }

        private static Update givenInvalidUpdateWithoutAuthor() {
            return createUpdateFrom();
        }

        private static Update givenInvalidUpdateWithoutChatId() {
            Update update = createUpdateFrom();
            update.getMessage().setFrom(createFromUser());
            update.getMessage().setChat(new Chat());

            return update;
        }

        private static Update createUpdateFrom() {
            Update update = new Update();
            Message message = new Message();
            message.setText(TestHelper.POO_MESSAGE);
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

        private static Secretion createExpectedPoo() {
            return new Secretion(TestHelper.USER_NAME, Instant.now(), TestHelper.CHAT_ID, SecretionType.POO);
        }

        private static Secretion createExpectedPuke() {
            return new Secretion(TestHelper.USER_NAME, Instant.now(), TestHelper.CHAT_ID, SecretionType.PUKE);
        }
    }
}
