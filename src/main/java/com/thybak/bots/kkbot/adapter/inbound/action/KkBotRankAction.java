package com.thybak.bots.kkbot.adapter.inbound.action;

import com.thybak.bots.kkbot.adapter.inbound.dto.ActionResponse;
import com.thybak.bots.kkbot.domain.model.SecretionRankEntry;
import com.thybak.bots.kkbot.domain.model.SecretionRankPeriod;
import com.thybak.bots.kkbot.domain.usecase.GetSecretionRankingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class KkBotRankAction implements KkBotAction {
    private static final String WRONG_PARAMETER_TO_RANK = "Bobo o k? Pídeme el ranking de 'hoy', 'semana', 'mes', 'año', no de lo que tú quieras";
    private static final String NO_RANK_ENTRIES_RETRIEVED = "Aquí hay que comer un poco más de fibra eh? Todavía no tengo datos para el periodo solicitado.";
    private static final String RANK_COMMAND = "/rank";
    private static final String RANK_HEADER_TEMPLATE = "Cagones! Aquí está el ranking de %s:%n";

    private final GetSecretionRankingUseCase getSecretionRankingUseCase;

    @Override
    public String getCommand() {
        return RANK_COMMAND;
    }

    @Override
    public ActionResponse executeAction(Update update) {
        final Message commandMessage = update.getMessage();
        final Optional<SecretionRankPeriod> period = getPooRankPeriodFrom(commandMessage.getText());
        if (period.isEmpty()) {
            return ActionResponse.builder().text(WRONG_PARAMETER_TO_RANK).build();
        }
        final List<SecretionRankEntry> secretionRanking = getSecretionRankingUseCase.run(period.get(), commandMessage.getChatId());
        return ActionResponse.builder().text(getSecretionRankingFormattedFrom(secretionRanking, period.get())).build();
    }

    private Optional<SecretionRankPeriod> getPooRankPeriodFrom(String command) {
        final String[] commandSplit = command.split(" ");
        final String period = commandSplit[1];
        return Arrays.stream(SecretionRankPeriod.values()).filter(pooRankPeriod -> pooRankPeriod.getPeriodName().equals(period)).findFirst();
    }

    private String getSecretionRankingFormattedFrom(List<SecretionRankEntry> secretionRanking, SecretionRankPeriod period) {
        final var sbRanking = new StringBuilder(String.format(RANK_HEADER_TEMPLATE, period.longPeriodName()));
        final var secretions = new ArrayList<>(secretionRanking);
        secretions.sort((rankEntry1, rankEntry2) -> rankEntry2.poos().compareTo(rankEntry1.poos()));

        if (secretions.isEmpty()) {
            return sbRanking.append(NO_RANK_ENTRIES_RETRIEVED).toString();
        }

        IntStream.range(0, secretions.size())
            .forEach(index -> {
                final var secretionRankEntry = secretions.get(index);
                if (secretionRankEntry.poos() == 0) {
                    return;
                }
                sbRanking.append(String.format("%d - %s con %d kks!%n", index + 1, secretionRankEntry.getUsernameWithEscapedUnderscore(), secretionRankEntry.poos()));
            });

        if (secretions.stream().anyMatch(secretionRankEntry -> secretionRankEntry.pukes() > 0)) {
            sbRanking.append(String.format("%nY los que más han vomitado son:%n"));
            secretions.sort((rankEntry1, rankEntry2) -> rankEntry2.pukes().compareTo(rankEntry1.pukes()));
            IntStream.range(0, secretions.size())
                    .forEach(index -> {
                        final var secretionRankEntry = secretions.get(index);
                        if (secretionRankEntry.pukes() == 0) {
                            return;
                        }
                        sbRanking.append(String.format("%d - %s con %d potas!%n", index + 1, secretionRankEntry.getUsernameWithEscapedUnderscore(), secretionRankEntry.pukes()));
                    });
        }

        return sbRanking.toString();
    }
}
