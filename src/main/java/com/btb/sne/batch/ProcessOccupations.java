package com.btb.sne.batch;

import com.btb.sne.data.Occupation;
import com.btb.sne.service.OccupationService;
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
public class ProcessOccupations {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;
    public final OccupationService service;

    @Bean
    public Step occupationsStep() {
        return this.stepBuilderFactory.get("Occupations")
                .<Occupation,Occupation>chunk(100)
                .reader(occupationsReader())
                .writer(occupationsWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<Occupation> occupationsReader() {
        final String[] fields = new String[]{"conceptType","conceptUri","iscoGroup","preferredLabel","altLabels","hiddenLabels","status","modifiedDate","regulatedProfessionNote","scopeNote","definition","inScheme","description","code"};

        return new FlatFileItemReaderBuilder<Occupation>()
                .name("occupationsReader")
                .resource(new ClassPathResource("occupations_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Occupation.class);
                }})
                .targetType(Occupation.class)
                .build();

    }

    @Bean
    public ItemWriter<Occupation> occupationsWriter() {
        return service::save;
    }
}
