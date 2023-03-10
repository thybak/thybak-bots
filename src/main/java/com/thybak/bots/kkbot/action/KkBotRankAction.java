package com.thybak.bots.kkbot.action;

import com.thybak.bots.kkbot.KkBotService;
import com.thybak.bots.kkbot.domain.ActionResponse;
import com.thybak.bots.kkbot.domain.PooRankEntry;
import com.thybak.bots.kkbot.domain.PooRankPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    private final KkBotService kkBotService;

    @Override
    public String getCommand() {
        return RANK_COMMAND;
    }

    @Override
    public ActionResponse executeAction(Update update) {
        Message commandMessage = update.getMessage();
        Optional<PooRankPeriod> period = getPooRankPeriodFrom(commandMessage.getText());
        if (period.isEmpty()) {
            return ActionResponse.builder().text(WRONG_PARAMETER_TO_RANK).build();
        }
        List<PooRankEntry> pooRanking = kkBotService.getPooRankingFrom(period.get(), commandMessage.getChatId());
        return ActionResponse.builder().text(getPooRankingFormattedFrom(pooRanking, period.get())).build();
    }

    private Optional<PooRankPeriod> getPooRankPeriodFrom(String command) {
        String[] commandSplit = command.split(" ");
        String period = commandSplit[1];
        return Arrays.stream(PooRankPeriod.values()).filter(pooRankPeriod -> pooRankPeriod.getPeriodName().equals(period)).findFirst();
    }

    private String getPooRankingFormattedFrom(List<PooRankEntry> pooRanking, PooRankPeriod period) {
        StringBuilder sbRanking = new StringBuilder(String.format(RANK_HEADER_TEMPLATE, period.longPeriodName()));

        if (pooRanking.isEmpty()) {
            sbRanking.append(NO_RANK_ENTRIES_RETRIEVED);
        } else {
            IntStream.range(0, pooRanking.size())
                    .forEach(index -> {
                        PooRankEntry pooRankEntry = pooRanking.get(index);
                        sbRanking.append(String.format("%d - %s con %d kks!%n", index + 1, pooRankEntry.getUsername(), pooRankEntry.getPoos()));
                    });
        }

        return sbRanking.toString();
    }
}
