package com.thybak.bots.kkbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.Arrays;

@Service
public class KkBotService {
    @Autowired
    PooRepository pooRepository;

    public boolean registerPooFrom(Update update) {
        if (!isAPoo(update)) {
            System.out.printf("No se ha enviado una kk %s%n", update);
            return false;
        }

        Message message = update.getMessage();
        if (!hasAuthorUsername(message)) {
            System.out.printf("El autor de la kk no tiene username registrado %s%n", update);
            return false;
        }
        if (message.getChatId() == null) {
            System.out.printf("El mensaje no tiene ningún chatId asociado, peligro! %s%n", update);
            return false;
        }

        Poo poo = new Poo(message.getFrom().getUserName(), Instant.now(), message.getChatId());
        try {
            System.out.printf("%s%n", pooRepository.save(poo));
            return true;
        } catch (Exception exception) {
            System.out.printf("Hubo un error almacenando en la base de datos el siguiente elemento -> %s%n", poo);
            return false;
        }
    }

    private boolean isAPoo(Update update) {
        if (!update.hasMessage())
            return false;

        Message message = update.getMessage();
        if (!message.hasText())
            return false;

        String text = message.getText();
        return Arrays.equals(text.getBytes(), Constants.POO_BYTES);
    }

    private boolean hasAuthorUsername(Message message) {
        return message.getFrom() != null && message.getFrom().getUserName() != null;
    }

    private static final class Constants {
        private static final byte[] POO_BYTES = new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92, (byte) 0xa9};
    }

}

//
