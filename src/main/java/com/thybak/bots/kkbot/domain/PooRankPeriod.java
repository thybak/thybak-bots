package com.thybak.bots.kkbot.domain;

import lombok.Getter;

@Getter
public enum PooRankPeriod {
    PAST_WEEK("past-week"),
    PAST_MONTH("past-month");

    private final String periodName;

    PooRankPeriod(String periodName){
        this.periodName = periodName;
    }

    public String longPeriodName() {
        return this == PooRankPeriod.PAST_WEEK ? "la semana pasada" : "este mes pasado";
    }
}
