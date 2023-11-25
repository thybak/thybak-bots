package com.thybak.bots.kkbot.adapter.inbound.action;

import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import com.thybak.bots.kkbot.domain.model.PooRankEntry;
import com.thybak.bots.kkbot.domain.model.PooRankPeriod;
import com.thybak.bots.kkbot.domain.usecase.GetSecretionRankingUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class KkBotRankActionTest {
    @Mock
    private GetSecretionRankingUseCase getSecretionRankingUseCase;

    @InjectMocks
    private KkBotRankAction sut;

    @Test
    void givenInvalidRankRequest_whenExecuteAction_thenWrongParameterErrorIsReturned() {
        Update invalidUpdate = TestHelper.givenInvalidPeriodUpdate();

        ActionResponse actionResponse = sut.executeAction(invalidUpdate);

        assertEquals(TestHelper.WRONG_PARAMETER_TO_RANK, actionResponse.getText());
        Mockito.verifyNoInteractions(getSecretionRankingUseCase);
    }

    @Test
    void givenRankRequestWithoutData_whenExecuteAction_thenNoRankEntriesErrorIsReturned() {
        Update validRankRequest = TestHelper.givenValidUpdate();
        Mockito.when(getSecretionRankingUseCase.run(PooRankPeriod.PAST_WEEK, TestHelper.CHAT_ID)).thenReturn(List.of());

        ActionResponse actionResponse = sut.executeAction(validRankRequest);
        assertEquals(TestHelper.NO_RANK_ENTRIES_RETRIEVED.replaceAll("\\r?\\n", System.getProperty("line.separator")), actionResponse.getText());
        Mockito.verify(getSecretionRankingUseCase).run(PooRankPeriod.PAST_WEEK, TestHelper.CHAT_ID);
    }

    @Test
    void givenRankRequestWithData_whenExecuteAction_thenRankEntriesAreReturned() {
        Update validRankRequest = TestHelper.givenValidUpdate();
        Mockito.when(getSecretionRankingUseCase.run(PooRankPeriod.PAST_WEEK, TestHelper.CHAT_ID)).thenReturn(TestHelper.RANK_ENTRIES);

        ActionResponse actionResponse = sut.executeAction(validRankRequest);

        assertEquals(TestHelper.RANK_ENTRIES_MESSAGE.replaceAll("\\r?\\n", System.getProperty("line.separator")), actionResponse.getText());
        Mockito.verify(getSecretionRankingUseCase).run(PooRankPeriod.PAST_WEEK, TestHelper.CHAT_ID);
    }

    private static final class TestHelper {
        private static final Long CHAT_ID = 1L;
        private static final String NO_RANK_ENTRIES_RETRIEVED = """
                Cagones! Aquí está el ranking de la semana pasada:
                Aquí hay que comer un poco más de fibra eh? Todavía no tengo datos para el periodo solicitado.""";
        private static final String WRONG_PARAMETER_TO_RANK = "Bobo o k? Pídeme el ranking de 'hoy', 'semana', 'mes', 'año', no de lo que tú quieras";
        private static final List<PooRankEntry> RANK_ENTRIES = List.of(
                new PooRankEntry("USERNAME1", 20L),
                new PooRankEntry("USERNAME2", 10L)
        );
        private static final String RANK_ENTRIES_MESSAGE = """
                Cagones! Aquí está el ranking de la semana pasada:
                1 - USERNAME1 con 20 kks!
                2 - USERNAME2 con 10 kks!
                """;

        private static Update givenValidUpdate() {
            Update update = createUpdate();
            update.getMessage().setText("/rank past-week");

            return update;
        }

        private static Update givenInvalidPeriodUpdate() {
            Update update = createUpdate();
            update.getMessage().setText("/rank asdf");

            return update;
        }

        private static Update createUpdate() {
            Update update = new Update();
            Message message = new Message();
            message.setChat(new Chat(CHAT_ID, ""));
            update.setMessage(message);

            return update;
        }
    }
}