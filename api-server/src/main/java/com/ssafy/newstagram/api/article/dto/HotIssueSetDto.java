package com.ssafy.newstagram.api.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotIssueSetDto {

    private String periodKey;
    private String periodType;

    // 클러스터(그룹) 단위 Hot Issue
    private List<HotIssueGroupDto> groups;
}

