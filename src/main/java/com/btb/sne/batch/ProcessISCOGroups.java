package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.ISCOGroup;
import com.btb.sne.service.ISCOGroupService;
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
public class ProcessISCOGroups {

    private final StepBuilderFactory stepBuilderFactory;
    private final ISCOGroupService service;
    private final ApplicationConfig config;

    @Bean("ProcessISCOGroups.step")
    public Step step() {
        return this.stepBuilderFactory.get("ISCO Groups")
                .<ISCOGroup, ISCOGroup>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessISCOGroups.reader")
    public FlatFileItemReader<ISCOGroup> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "code", "preferredLabel", "altLabels", "inScheme", "description"};

        return new FlatFileItemReaderBuilder<ISCOGroup>()
                .name("ProcessISCOGroups Reader")
                .resource(new ClassPathResource("ISCOGroups_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(ISCOGroup.class)
                .build();

    }

    @Bean("ProcessISCOGroups.writer")
    public ItemWriter<ISCOGroup> itemWriter() {
        return service::save;
    }
}
