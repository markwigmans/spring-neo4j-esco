package com.btb.sne.batch;

import com.btb.sne.data.SkillGroup;
import com.btb.sne.service.SkillGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
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

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;
    public final SkillGroupService service;

    @Bean
    public Step skillGroupsStep() {
        return this.stepBuilderFactory.get("Skill Groups")
                .<SkillGroup,SkillGroup>chunk(100)
                .reader(skillGroupsReader())
                .writer(skillGroupsWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<SkillGroup> skillGroupsReader() {
        final String[] fields = new String[]{"conceptType","conceptUri","preferredLabel","altLabels","hiddenLabels","status","modifiedDate","scopeNote","inScheme","description","code"};

        return new FlatFileItemReaderBuilder<SkillGroup>()
                .name("skillGroupsReader")
                .resource(new ClassPathResource("skillGroups_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(SkillGroup.class);
                }})
                .targetType(SkillGroup.class)
                .build();

    }

    @Bean
    public ItemWriter<SkillGroup> skillGroupsWriter() {
        return service::save;
    }
}
