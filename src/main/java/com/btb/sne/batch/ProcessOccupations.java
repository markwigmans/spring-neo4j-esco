package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Occupation;
import com.btb.sne.service.OccupationService;
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
public class ProcessOccupations {

    private final StepBuilderFactory stepBuilderFactory;
    private final OccupationService service;
    private final ApplicationConfig config;

    @Bean("ProcessOccupations.step")
    public Step step() {
        return this.stepBuilderFactory.get("Occupations")
                .<Occupation, Occupation>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessOccupations.reader")
    @StepScope
    public FlatFileItemReader<Occupation> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "iscoGroup", "preferredLabel", "altLabels", "hiddenLabels", "status", "modifiedDate", "regulatedProfessionNote", "scopeNote", "definition", "inScheme", "description", "code"};

        return new FlatFileItemReaderBuilder<Occupation>()
                .name("ProcessOccupations Reader")
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

    @Bean("ProcessOccupations.writer")
    public ItemWriter<Occupation> itemWriter() {
        return service::save;
    }
}
