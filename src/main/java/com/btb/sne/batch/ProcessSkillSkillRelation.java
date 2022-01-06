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
public class ProcessSkillSkillRelation {

    private final StepBuilderFactory stepBuilderFactory;
    private final ApplicationConfig config;
    private final Readers readers;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;

    @Bean("ProcessSkillSkillRelation.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Neo4j - Skill Skill relations")
                .<Readers.SkillSkillRelation, Readers.SkillSkillRelation>chunk(config.getChunkSize())
                .reader(readers.skillSkillRelationItemReader())
                .writer(neoWriters.skillSkillRelationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessSkillSkillRelation.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("JPA - Skill Skill relations")
                .transactionManager(jpaWriters.transactionManager(null))
                .<Readers.SkillSkillRelation, Readers.SkillSkillRelation>chunk(config.getChunkSize())
                .reader(readers.skillSkillRelationItemReader())
                .writer(jpaWriters.skillSkillRelationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }
}
