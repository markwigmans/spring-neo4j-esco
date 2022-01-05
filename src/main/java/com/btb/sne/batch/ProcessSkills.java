package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Skill;
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
public class ProcessSkills {

    private final StepBuilderFactory stepBuilderFactory;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;
    private final ApplicationConfig config;

    @Bean("ProcessSkills.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Skills")
                .<Skill, Skill>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(neoWriters.skillItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessSkills.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("Skills")
                .<Skill, Skill>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(jpaWriters.skillItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean
    public FlatFileItemReader<Skill> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "skillType", "reuseLevel", "preferredLabel", "altLabels", "hiddenLabels", "status", "modifiedDate", "scopeNote", "definition", "inScheme", "description"};

        return new FlatFileItemReaderBuilder<Skill>()
                .name("ProcessSkills Reader")
                .resource(new ClassPathResource("skills_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(Skill.class)
                .build();
    }
}
