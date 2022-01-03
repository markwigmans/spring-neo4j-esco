package com.btb.sne.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableBatchProcessing
@Primary
@RequiredArgsConstructor
public class BatchConfiguration extends DefaultBatchConfigurer {

    private final ProcessSkills processSkills;
    private final ProcessSkillGroups processSkillGroups;
    private final ProcessOccupations processOccupations;
    private final ProcessISCOGroups processISCOGroups;
    private final ProcessBroaderOccupations processBroaderOccupations;
    private final ProcessBroaderSkills processBroaderSkills;
    private final ProcessSkillSkillRelation processSkillSkillRelation;
    private final ProcessOccupationalSkillRelation processOccupationalSkillRelation;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        return jobBuilderFactory.get("ESCO job")
                .incrementer(new RunIdIncrementer())
                .start(processSkills.step())
                .next(processSkillGroups.step())
                .next(processOccupations.step())
                .next(processISCOGroups.step())
                .next(processBroaderOccupations.step())
                .next(processBroaderSkills.step())
                .next(processSkillSkillRelation.step())
                .next(processOccupationalSkillRelation.step())
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
                .build();
    }
}