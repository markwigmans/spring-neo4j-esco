package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ProcessTransversals {

    private final StepBuilderFactory stepBuilderFactory;
    private final ApplicationConfig config;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;
    private final Readers readers;
    private final PlatformTransactionManager tm;

    @Bean("ProcessTransversals.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Neo4j - Transversals")
                .<Readers.TransversalInput, Readers.TransversalInput>chunk(config.getChunkSize())
                .reader(multiItemReaders())
                .writer(neoWriters.transversalInputSkillItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessTransversals.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("JPA - Transversals")
                .transactionManager(tm)
                .<Readers.TransversalInput, Readers.TransversalInput>chunk(config.getChunkSize())
                .reader(multiItemReaders())
                .writer(jpaWriters.transversalInputSkillItemWriter())
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
