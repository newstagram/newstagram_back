package com.ssafy.newstagram.api.article.util.period;

import java.time.LocalDateTime;

public class RealtimePeriodCalculator implements PeriodCalculator {

    @Override
    public LocalDateTime getStart() {
        return LocalDateTime.now().minusHours(1);
    }

    @Override
    public LocalDateTime getEnd() {
        return LocalDateTime.now();
    }
}
