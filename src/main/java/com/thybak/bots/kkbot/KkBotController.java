package com.thybak.bots.kkbot;

import com.thybak.bots.kkbot.domain.PooRankPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("v1/kkbot")
public class KkBotController {

    @Autowired
    private KkBotScheduler kkBotScheduler;

    @GetMapping("/rank")
    @ResponseBody
    public void showRank(@RequestParam() String period) {
        Optional<PooRankPeriod> pooRankPeriod = Arrays.stream(PooRankPeriod.values()).filter(pooRankPeriodEntry -> pooRankPeriodEntry.getPeriodName().equals(period)).findFirst();
        if (pooRankPeriod.isEmpty())
            return;

        if (pooRankPeriod.get() == PooRankPeriod.PAST_WEEK) {
            kkBotScheduler.publishWeeklyRank();
        } else {
            kkBotScheduler.publishMonthlyRank();
        }
    }
}
