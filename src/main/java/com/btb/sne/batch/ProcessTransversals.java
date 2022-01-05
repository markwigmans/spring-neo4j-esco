package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class ProcessTransversals {

    private final StepBuilderFactory stepBuilderFactory;
    private final ApplicationConfig config;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;
    private final NeoProcessors neoProcessors;
    private final JpaProcessors jpaProcessors;
    private final Readers readers;

    @Bean("ProcessTransversals.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Transversals")
                .<Readers.TransversalInput, Skill>chunk(config.getChunkSize())
                .reader(multiItemReaders())
                .processor(neoProcessors.transversalInputItemProcessor())
                .writer(neoWriters.skillItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessTransversals.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("Transversals")
                .<Readers.TransversalInput, Skill>chunk(config.getChunkSize())
                .reader(multiItemReaders())
                .processor(jpaProcessors.transversalInputItemProcessor())
                .writer(jpaWriters.skillItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessTransversals.readers")
    public MultiResourceItemReader<Readers.TransversalInput> multiItemReaders() {
        Resource[] inputFiles = {
                new ClassPathResource("transversalSkillsCollection_nl.csv"),
                new ClassPathResource("ictSkillsCollection_nl.csv"),
                new ClassPathResource("languageSkillsCollection_nl.csv")
        };

        return new MultiResourceItemReaderBuilder<Readers.TransversalInput>()
                .name("multiCustomerReader")
                .resources(inputFiles)
                .delegate(readers.transversalInputItemReader())
                .build();
    }
}
