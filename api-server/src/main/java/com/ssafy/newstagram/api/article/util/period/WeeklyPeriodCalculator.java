package com.ssafy.newstagram.api.article.util.period;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class WeeklyPeriodCalculator implements PeriodCalculator {

    @Override
    public LocalDateTime getStart() {
        LocalDate lastWeekMonday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(1);

        return lastWeekMonday.atStartOfDay();
    }

    @Override
    public LocalDateTime getEnd() {
        LocalDate lastWeekSunday = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(1)
                .plusDays(6);

        return lastWeekSunday.atTime(23, 59, 59);
    }
}
