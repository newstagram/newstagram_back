package com.ssafy.newstagram.api.article.util.period;

import java.time.LocalDateTime;

public interface PeriodCalculator {
    LocalDateTime getStart();
    LocalDateTime getEnd();
}
