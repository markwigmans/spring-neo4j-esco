package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessOccupationalSkillRelation {

    private final StepBuilderFactory stepBuilderFactory;
    private final ApplicationConfig config;
    private final Readers readers;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;

    @Bean("ProcessOccupationalSkillRelation.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Occupational Skill relations")
                .<Readers.OccupationalSkillRelation, Readers.OccupationalSkillRelation>chunk(config.getChunkSize())
                .reader(readers.occupationalSkillRelationItemReader())
                .writer(neoWriters.occupationalSkillRelationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessOccupationalSkillRelation.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("Occupational Skill relations")
                .<Readers.OccupationalSkillRelation, Readers.OccupationalSkillRelation>chunk(config.getChunkSize())
                .reader(readers.occupationalSkillRelationItemReader())
                .writer(jpaWriters.occupationalSkillRelationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }
}

