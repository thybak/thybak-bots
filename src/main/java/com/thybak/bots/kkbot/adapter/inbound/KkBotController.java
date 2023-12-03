package com.thybak.bots.kkbot.adapter.inbound;

import com.thybak.bots.kkbot.domain.model.SecretionRankPeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("v1/kkbot")
@RequiredArgsConstructor
public class KkBotController {

    private final KkBotScheduler kkBotScheduler;

    @GetMapping("/rank")
    @ResponseBody
    public void showRank(@RequestParam() String period) {
        final Optional<SecretionRankPeriod> pooRankPeriod = Arrays.stream(SecretionRankPeriod.values()).filter(pooRankPeriodEntry -> pooRankPeriodEntry.getPeriodName().equals(period)).findFirst();
        if (pooRankPeriod.isEmpty())
            return;

        if (pooRankPeriod.get() == SecretionRankPeriod.PAST_WEEK) {
            kkBotScheduler.publishWeeklyRank();
        } else {
            kkBotScheduler.publishMonthlyRank();
        }
    }
}
