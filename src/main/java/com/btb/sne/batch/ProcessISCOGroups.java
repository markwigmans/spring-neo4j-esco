package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.ISCOGroup;
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
public class ProcessISCOGroups {

    private final StepBuilderFactory stepBuilderFactory;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;
    private final ApplicationConfig config;

    @Bean("ProcessISCOGroups.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Neo4j - ISCO Groups")
                .<ISCOGroup, ISCOGroup>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(neoWriters.iscoGroupItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessISCOGroups.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("JPA - ISCO Groups")
                .transactionManager(jpaWriters.transactionManager(null))
                .<ISCOGroup, ISCOGroup>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(jpaWriters.iscoGroupItemWriter())
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
}
