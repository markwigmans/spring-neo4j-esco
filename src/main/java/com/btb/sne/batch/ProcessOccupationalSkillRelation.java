package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Occupation;
import com.btb.sne.model.Skill;
import com.btb.sne.service.OccupationService;
import com.btb.sne.service.SkillService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessOccupationalSkillRelation {

    private final StepBuilderFactory stepBuilderFactory;
    private final SkillService skillService;
    private final OccupationService occupationService;
    private final ApplicationConfig config;

    @Bean("ProcessOccupationalSkillRelation.step")
    public Step step() {
        return this.stepBuilderFactory.get("Occupational Skill relations")
                .<OccupationalSkillRelation, OccupationalSkillRelation>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessOccupationalSkillRelation.reader")
    @StepScope
    public FlatFileItemReader<OccupationalSkillRelation> itemReader() {
        final String[] fields = new String[]{"occupationUri","relationType","skillType","skillUri"};

        return new FlatFileItemReaderBuilder<OccupationalSkillRelation>()
                .name("ProcessOccupationalSkillRelation Reader")
                .resource(new ClassPathResource("occupationSkillRelations.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{setTargetType(OccupationalSkillRelation.class);}})
                .targetType(OccupationalSkillRelation.class)
                .build();
    }

    @Bean("ProcessOccupationalSkillRelation.writer")
    public ItemWriter<OccupationalSkillRelation> itemWriter() {
        return items -> {
            // group By occupation
            Map<String, ? extends List<? extends OccupationalSkillRelation>> collect =
                    items.stream().collect(groupingBy(OccupationalSkillRelation::getOccupationUri));

            collect.forEach((key, value) -> {
                final Set<Skill> optionals = new HashSet<>();
                final Set<Skill> essentials = new HashSet<>();
                Optional<Occupation> occupation = occupationService.get(key);
                if (occupation.isPresent()) {
                    value.forEach(s -> {
                        Optional<Skill> skill = skillService.get(s.getSkillUri());
                        if (skill.isPresent()) {
                            switch (s.getRelationType()) {
                                case "optional" -> optionals.add(skill.get());
                                case "essential" -> essentials.add(skill.get());
                                default -> log.warn("unknown type: {}", s.getRelationType());
                            }
                        }
                    });
                    occupation.get().getOptionalSkills().addAll(optionals);
                    occupation.get().getEssentialSkills().addAll(essentials);
                    occupationService.save(occupation.get());
                }
            });
        };
    }

    @Data
    public static class OccupationalSkillRelation {
         private String occupationUri;
         private String relationType;
         private String skillType;
         private String skillUri;
    }
}

