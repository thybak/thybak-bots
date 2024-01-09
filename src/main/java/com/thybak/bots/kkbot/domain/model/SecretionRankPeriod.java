package com.thybak.bots.kkbot.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

@Getter
@RequiredArgsConstructor
public enum SecretionRankPeriod {
    PAST_WEEK("past-week"),
    PAST_MONTH("past-month"),
    PAST_YEAR("past-year");


    private static final String SPAIN_LOCAL_ZONE = "Europe/Madrid";
    private static final ZoneId ZONE_ID = ZoneId.of(SPAIN_LOCAL_ZONE);
    private final String periodName;

    public String longPeriodName() {
        return switch (this) {
            case PAST_WEEK -> "la semana pasada";
            case PAST_MONTH -> "este mes pasado";
            case PAST_YEAR -> "año nuevo";
        };
    }

    public Instant initialInstant(final Clock clock) {
        return switch (this) {
            case PAST_WEEK -> {
                LocalDate pastWeekFirstDay = LocalDate.now(clock).minusWeeks(1L).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                yield pastWeekFirstDay.atStartOfDay(ZONE_ID).toInstant();
            }
            case PAST_MONTH -> {
                LocalDate pastMonthFirstDay = LocalDate.now(clock).minusMonths(1);
                yield LocalDate.of(pastMonthFirstDay.getYear(), pastMonthFirstDay.getMonthValue(), 1).atStartOfDay(ZONE_ID).toInstant();
            }
            case PAST_YEAR -> {
                LocalDate pastYearFirstDay = LocalDate.now(clock).minusYears(1);
                yield LocalDate.of(pastYearFirstDay.getYear(), pastYearFirstDay.getMonthValue(), 1).atStartOfDay(ZONE_ID).toInstant();
            }
        };
    }

    public Instant finalInstant(final Clock clock) {
        return switch (this) {
            case PAST_WEEK -> {
                LocalDate pastWeekLastDay = LocalDate.now(clock).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                yield pastWeekLastDay.atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
            }
            case PAST_MONTH -> {
                LocalDate pastMonthLastDate = LocalDate.now(clock).minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                yield pastMonthLastDate.atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
            }
            case PAST_YEAR -> {
                LocalDate pastYearLastDate = LocalDate.now(clock).minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                yield pastYearLastDate.atTime(LocalTime.MAX).atZone(ZONE_ID).toInstant();
            }
        };
    }
}
