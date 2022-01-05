package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessBroaderSkills {

    private static final String SKILL_GROUP_TYPE = "SkillGroup";

    private final StepBuilderFactory stepBuilderFactory;
    private final ApplicationConfig config;
    private final Readers readers;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;

    @Bean("ProcessBroaderSkills.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Broader Skill relations")
                .<Readers.BroaderSkill, Readers.BroaderSkill>chunk(config.getChunkSize())
                .reader(readers.broaderSkillItemReader())
                .writer(neoWriters.broaderSkillItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessBroaderSkills.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("Broader Skill relations")
                .<Readers.BroaderSkill, Readers.BroaderSkill>chunk(config.getChunkSize())
                .reader(readers.broaderSkillItemReader())
                .writer(jpaWriters.broaderSkillItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    public static class BroaderOccupationClassifier implements Classifier<Readers.BroaderSkill, ItemWriter<? super Readers.BroaderSkill>> {
        private final ItemWriter<Readers.BroaderSkill> groupGroupWriter;
        private final ItemWriter<Readers.BroaderSkill> skillSkillGroupWriter;
        private final ItemWriter<Readers.BroaderSkill> SkillSkillWriter;

        public BroaderOccupationClassifier(ItemWriter<Readers.BroaderSkill> w1,
                                           ItemWriter<Readers.BroaderSkill> w2,
                                           ItemWriter<Readers.BroaderSkill> w3) {
            this.groupGroupWriter = w1;
            this.skillSkillGroupWriter = w2;
            this.SkillSkillWriter = w3;
        }

        @Override
        public ItemWriter<? super Readers.BroaderSkill> classify(Readers.BroaderSkill bs) {
            if (bs.getConceptType().equals(SKILL_GROUP_TYPE)) {
                return groupGroupWriter;
            } else if (bs.getBroaderType().equals(SKILL_GROUP_TYPE)) {
                return skillSkillGroupWriter;
            } else {
                return SkillSkillWriter;
            }
        }
    }
 }
