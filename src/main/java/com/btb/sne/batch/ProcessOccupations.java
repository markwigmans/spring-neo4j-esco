package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Occupation;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class ProcessOccupations {

    private final StepBuilderFactory stepBuilderFactory;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;
    private final ApplicationConfig config;

    @Bean("ProcessOccupations.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Neo4j - Occupations")
                .<Occupation, Occupation>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(neoWriters.occupationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessOccupations.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("JPA - Occupations")
                .transactionManager(jpaWriters.transactionManager(null))
                .<Occupation, Occupation>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(jpaWriters.occupationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessOccupations.reader")
    public FlatFileItemReader<Occupation> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "iscoGroup", "preferredLabel", "altLabels", "hiddenLabels", "status", "modifiedDate", "regulatedProfessionNote", "scopeNote", "definition", "inScheme", "description", "code"};

        return new FlatFileItemReaderBuilder<Occupation>()
                .name("ProcessOccupations Reader")
                .resource(new ClassPathResource("occupations_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(Occupation.class)
                .build();
    }
}
