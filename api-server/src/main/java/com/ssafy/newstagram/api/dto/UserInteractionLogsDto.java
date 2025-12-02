package com.ssafy.newstagram.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInteractionLogsDto {
    private Long id;
    @Builder.Default
    private String interaction_type = "CLICK";
    private LocalDateTime created_at;
    private String session_id;
    private String user_agent;
    private String ip_address;
    private Long user_id;
    private Long article_id;
}
