package com.thybak.bots.kkbot.adapter.inbound;

import com.thybak.bots.kkbot.domain.model.PooRankPeriod;
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
        final Optional<PooRankPeriod> pooRankPeriod = Arrays.stream(PooRankPeriod.values()).filter(pooRankPeriodEntry -> pooRankPeriodEntry.getPeriodName().equals(period)).findFirst();
        if (pooRankPeriod.isEmpty())
            return;

        if (pooRankPeriod.get() == PooRankPeriod.PAST_WEEK) {
            kkBotScheduler.publishWeeklyRank();
        } else {
            kkBotScheduler.publishMonthlyRank();
        }
    }
}
