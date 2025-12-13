package com.ssafy.newstagram.rss.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="system_job_logs")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsBatchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="job_name", nullable = false, length=50)
    private String jobName;

    @Column(name="run_date", nullable = false)
    private LocalDateTime runDate;

    @Column(name="status", nullable = false, length = 20)
    private String status;

    @Column(name="message")
    private String message;

    @Column(name="items_processed")
    private Integer itemsProcessed;

    @Column(name="retry_count")
    private Integer retryCount;

    @Column(name="started_at")
    private LocalDateTime startedAt;

    @Column(name="ended_at")
    private LocalDateTime endedAt;

    @Column(name="feed_id")
    private Long feedId;
}
