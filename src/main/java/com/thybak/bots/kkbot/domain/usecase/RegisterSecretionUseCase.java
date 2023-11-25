package com.thybak.bots.kkbot.domain.usecase;

import com.thybak.bots.kkbot.domain.model.Secretion;
import com.thybak.bots.kkbot.domain.model.SecretionType;
import com.thybak.bots.kkbot.domain.provider.SecretionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

@RequiredArgsConstructor
public class RegisterSecretionUseCase {
    private final Logger logger = LoggerFactory.getLogger(RegisterSecretionUseCase.class);
    private final SecretionRepository secretionRepository;

    public boolean run(Update update, SecretionType secretionType) {
        final Message message = update.getMessage();
        if (!hasAuthorUsername(message)) {
            logger.error("El autor del {} no tiene username registrado {}", secretionType.toString(), update);
            return false;
        }
        if (message.getChatId() == null) {
            logger.error("El mensaje no tiene ningún chatId asociado, peligro! {}", update);
            return false;
        }

        final Secretion secretion = new Secretion(message.getFrom().getUserName(), Instant.now(), message.getChatId(), secretionType);
        try {
            logger.info("Secreción {} satisfactoriamente registrada {}", secretionType, secretionRepository.save(secretion));
            return true;
        } catch (Exception exception) {
            logger.error("Hubo un error almacenando en la base de datos el siguiente elemento -> {}", secretion);
            return false;
        }
    }

    private boolean hasAuthorUsername(Message message) {
        return message.getFrom() != null && message.getFrom().getUserName() != null;
    }
}
