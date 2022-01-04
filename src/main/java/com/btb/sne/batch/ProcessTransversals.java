package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Skill;
import com.btb.sne.model.SkillGroup;
import com.btb.sne.service.SkillGroupService;
import com.btb.sne.service.SkillService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ProcessTransversals {

    private final StepBuilderFactory stepBuilderFactory;
    private final SkillService skillService;
    private final SkillGroupService skillGroupService;
    private final ApplicationConfig config;
    private final ItemWriter<Skill> itemWriter;

    @Bean("ProcessTransversals.step")
    public Step step() {
        return this.stepBuilderFactory.get("Transversals")
                .<TransversalInput, Skill>chunk(config.getChunkSize())
                .reader(multiCustomerReader())
                .processor(itemProcessor())
                .writer(itemWriter)
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessTransversals.readers")
    public MultiResourceItemReader<TransversalInput> multiCustomerReader() {
        Resource[] inputFiles = {
                new ClassPathResource("transversalSkillsCollection_nl.csv"),
                new ClassPathResource("ictSkillsCollection_nl.csv"),
                new ClassPathResource("languageSkillsCollection_nl.csv")
        };

        return new MultiResourceItemReaderBuilder<TransversalInput>()
                .name("multiCustomerReader")
                .resources(inputFiles)
                .delegate(itemReader())
                .build();
    }

    @Bean("ProcessTransversals.reader")
    public FlatFileItemReader<TransversalInput> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "skillType", "reuseLevel", "preferredLabel", "altLabels", "description", "broaderConceptUri", "broaderConceptPT"};

        return new FlatFileItemReaderBuilder<TransversalInput>()
                .name("ProcessTransversals Reader")
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(TransversalInput.class)
                .build();
    }

    @Bean
    public ItemProcessor<TransversalInput, Skill> itemProcessor() {
        return new ConverterProcessor();
    }

    class ConverterProcessor implements ItemProcessor<TransversalInput, Skill> {

        @Override
        public Skill process(TransversalInput item) throws Exception {
            final Skill skill = skillService.get(item.conceptUri).orElseGet(() -> {
                Skill.SkillBuilder builder = Skill.builder();
                builder.conceptType(item.conceptType);
                builder.conceptUri(item.conceptUri);
                builder.skillType(item.skillType);
                builder.reuseLevel(item.reuseLevel);
                builder.preferredLabel(item.preferredLabel);
                builder.altLabels(item.altLabels);
                builder.description(item.description);
                return skillService.save(builder.build());
            });
            String[] items = item.broaderConceptUri.split("\\|");
            skill.getBroaderNodes().addAll(getSkills(items));
            skill.getBroaderGroup().addAll(getSkillGroups(items));
            return skill;
        }

        List<Skill> getSkills(String[] items) {
            return Arrays.stream(items).map(String::trim).map(skillService::get).flatMap(Optional::stream).toList();
        }

        List<SkillGroup> getSkillGroups(String[] items) {
            return Arrays.stream(items).map(String::trim).map(skillGroupService::get).flatMap(Optional::stream).toList();
        }
    }

    @Data
    public static class TransversalInput {
        private String conceptType;
        private String conceptUri;
        private String skillType;
        private String reuseLevel;
        private String preferredLabel;
        private String altLabels;
        private String description;
        private String broaderConceptUri;
        private String broaderConceptPT;
    }
}
