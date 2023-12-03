package com.thybak.bots.kkbot.domain.model;

import lombok.Getter;

@Getter
public enum SecretionRankPeriod {
    PAST_WEEK("past-week"),
    PAST_MONTH("past-month");

    private final String periodName;

    SecretionRankPeriod(String periodName){
        this.periodName = periodName;
    }

    public String longPeriodName() {
        return this == SecretionRankPeriod.PAST_WEEK ? "la semana pasada" : "este mes pasado";
    }
}
