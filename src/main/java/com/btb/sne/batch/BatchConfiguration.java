package com.btb.sne.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
    private final ProcessTransversals processTransversals;
    private final JobLoggerListener jobLoggerListener;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("ESCO job")
                .incrementer(new RunIdIncrementer())
                .start(neoFlow())
                .next(jpaFlow())
                .end()
                .listener(jobLoggerListener)
                .build();
    }

    public Flow neoFlow() {
        return new FlowBuilder<Flow>("Neo4J flow")
                .start(processSkills.neoStep())
                .next(processSkillGroups.neoStep())
                .next(processTransversals.neoStep())
                .next(processSkillSkillRelation.neoStep())
                .next(processBroaderSkills.neoStep())
                .next(processOccupations.neoStep())
                .next(processISCOGroups.neoStep())
                .next(processBroaderOccupations.neoStep())
                .next(processOccupationalSkillRelation.neoStep())
                .build();
    }

    public Flow jpaFlow() {
        return new FlowBuilder<Flow>("JPA flow")
                .start(processSkills.jpaStep())
                .next(processSkillGroups.jpaStep())
                .next(processTransversals.jpaStep())
                .next(processSkillSkillRelation.jpaStep())
                .next(processBroaderSkills.jpaStep())
                .next(processOccupations.jpaStep())
                .next(processISCOGroups.jpaStep())
                .next(processBroaderOccupations.jpaStep())
                .next(processOccupationalSkillRelation.jpaStep())
                .build();
    }
}