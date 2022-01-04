package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.SkillGroup;
import com.btb.sne.service.SkillGroupService;
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
public class ProcessSkillGroups {

    private final StepBuilderFactory stepBuilderFactory;
    private final SkillGroupService service;
    private final ApplicationConfig config;

    @Bean("ProcessSkillGroups.step")
    public Step step() {
        return this.stepBuilderFactory.get("Skill Groups")
                .<SkillGroup, SkillGroup>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessSkillGroups.reader")
    public FlatFileItemReader<SkillGroup> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "preferredLabel", "altLabels", "hiddenLabels", "status", "modifiedDate", "scopeNote", "inScheme", "description", "code"};

        return new FlatFileItemReaderBuilder<SkillGroup>()
                .name("ProcessSkillGroups Reader")
                .resource(new ClassPathResource("skillGroups_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(SkillGroup.class)
                .build();

    }

    @Bean("ProcessSkillGroups.writer")
    public ItemWriter<SkillGroup> itemWriter() {
        return service::save;
    }
}
