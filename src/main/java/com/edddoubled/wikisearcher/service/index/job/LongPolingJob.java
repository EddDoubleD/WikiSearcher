package com.edddoubled.wikisearcher.service.index.job;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class LongPolingJob implements Job {

    MessageHandler handler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        try {
            log.info("start execution task {}", jobName);
            handler.handleMessage();
        } catch (ExecutionException | InterruptedException e) {
            log.error("There was an error during the execution of the task {}, details: {}", jobName, e.getMessage());
        }
    }
}
