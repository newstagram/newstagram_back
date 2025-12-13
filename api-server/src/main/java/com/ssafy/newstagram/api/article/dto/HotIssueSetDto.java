package com.ssafy.newstagram.api.article.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotIssueSetDto {

    private String periodKey;
    private String periodType;
//    private LocalDateTime periodStart;
//    private LocalDateTime periodEnd;
    private List<Long> items;    // 포함된 기사 id 리스트

}
