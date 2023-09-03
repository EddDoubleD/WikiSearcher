package com.edddoubled.wikisearcher.config;

import com.edddoubled.wikisearcher.service.index.job.LongPolingJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(LongPolingJob.class)
                .storeDurably()
                .withIdentity("long_poling_sqs")
                .withDescription("task to survey the data provider for indexing")
                .storeDurably(true)
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("Qrtz_Trigger_Long_pooleng")
                .withDescription("trigger for starting a job to survey the data provider for indexing")
                .withSchedule(simpleSchedule()
                        .repeatForever()
                        .withIntervalInSeconds(20)
                )
                .build();
    }
}
