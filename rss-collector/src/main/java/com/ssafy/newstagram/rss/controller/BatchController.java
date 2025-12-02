package com.ssafy.newstagram.rss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final JobLauncher jobLauncher;
    private final Job rssMasterJob;

    @PostMapping("/rss")
    public Map<String, Object> startBatch() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("runId", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(rssMasterJob, jobParameters);

        Map<String, Object> result = new HashMap<>();
        result.put("jobId", execution.getJobId());
        result.put("status", execution.getStatus().toString());
        result.put("startTime", execution.getStartTime());
        result.put("endTime", execution.getEndTime());

        return result;
    }

}
