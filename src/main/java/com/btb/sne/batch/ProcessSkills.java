package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Skill;
import com.btb.sne.service.SkillService;
import lombok.RequiredArgsConstructor;
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

@Configuration
@RequiredArgsConstructor
public class ProcessSkills {

    private final StepBuilderFactory stepBuilderFactory;
    private final SkillService service;
    private final ApplicationConfig config;

    @Bean("ProcessSkills.step")
    public Step step() {
        return this.stepBuilderFactory.get("Skills")
                .<Skill, Skill>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessSkills.reader")
    @StepScope
    public FlatFileItemReader<Skill> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "skillType", "reuseLevel", "preferredLabel", "altLabels", "hiddenLabels", "status", "modifiedDate", "scopeNote", "definition", "inScheme", "description"};

        return new FlatFileItemReaderBuilder<Skill>()
                .name("ProcessSkills Reader")
                .resource(new ClassPathResource("skills_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Skill.class);
                }})
                .targetType(Skill.class)
                .build();

    }

    @Bean("ProcessSkills.writer")
    public ItemWriter<Skill> itemWriter() {
        return service::save;
    }
}
