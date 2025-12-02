package com.ssafy.newstagram.api.article.util.period;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyPeriodCalculator implements PeriodCalculator {

    @Override
    public LocalDateTime getStart() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return yesterday.atStartOfDay();  // 00:00:00
    }

    @Override
    public LocalDateTime getEnd() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return yesterday.atTime(23, 59, 59);
    }
}
