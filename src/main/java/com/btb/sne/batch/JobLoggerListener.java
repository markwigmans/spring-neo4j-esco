package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobLoggerListener {

    private static final String START_MESSAGE = "%s is beginning execution";
    private static final String END_MESSAGE = "%s has completed with the status %s";

    private final ApplicationConfig config;
    private final InitService initService;

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        log.info(String.format(START_MESSAGE, jobExecution.getJobInstance().getJobName()));
        if (config.isDelete()) {
            initService.deleteAll();
        }
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        log.info(String.format(END_MESSAGE,
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus()));
    }
}
