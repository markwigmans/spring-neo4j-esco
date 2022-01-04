package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Skill;
import com.btb.sne.service.SkillService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessSkillSkillRelation {

    private final StepBuilderFactory stepBuilderFactory;
    private final SkillService skillService;
    private final ApplicationConfig config;

    @Bean("ProcessSkillSkillRelation.step")
    public Step step() {
        return this.stepBuilderFactory.get("Skill Skill relations")
                .<SkillSkillRelation, SkillSkillRelation>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessSkillSkillRelation.reader")
    @StepScope
    public FlatFileItemReader<SkillSkillRelation> itemReader() {
        final String[] fields = new String[]{"originalSkillUri", "originalSkillType", "relationType", "relatedSkillType", "relatedSkillUri"};

        return new FlatFileItemReaderBuilder<SkillSkillRelation>()
                .name("ProcessSkillSkillRelation Reader")
                .resource(new ClassPathResource("skillSkillRelations.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(SkillSkillRelation.class);
                }})
                .targetType(SkillSkillRelation.class)
                .build();
    }

    @Bean("ProcessSkillSkillRelation.writer")
    public ItemWriter<SkillSkillRelation> itemWriter() {
        return items -> {
            for (SkillSkillRelation item : items) {
                Optional<Skill> skill1 = skillService.get(item.originalSkillUri);
                Optional<Skill> skill2 = skillService.get(item.relatedSkillUri);
                if (skill1.isPresent() && skill2.isPresent()) {
                    switch (item.relationType) {
                        case "optional" -> skill1.get().getOptionalSkills().add(skill2.get());
                        case "essential" -> skill1.get().getEssentialSkills().add(skill2.get());
                        default -> log.warn("unknown type: {}", item.relationType);
                    }
                    skillService.save(skill1.get());
                } else {
                    log.warn("{} : {} : {} : {}", item.originalSkillUri, skill1.isPresent(), item.relatedSkillUri, skill1.isPresent());
                }
            }
        };
    }

    @Data
    public static class SkillSkillRelation {
        private String originalSkillUri;
        private String originalSkillType;
        private String relationType;
        private String relatedSkillType;
        private String relatedSkillUri;
    }
}
