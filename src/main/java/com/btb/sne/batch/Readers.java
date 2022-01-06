package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class Readers {

    private final ApplicationConfig config;

    @Bean
    public FlatFileItemReader<BroaderOccupation> broaderOccupationItemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "broaderType", "broaderUri"};

        return new FlatFileItemReaderBuilder<BroaderOccupation>()
                .name("ProcessBroaderOccupations Reader")
                .resource(new ClassPathResource("broaderRelationsOccPillar.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(BroaderOccupation.class)
                .maxItemCount(config.getMaxCount())
                .build();
    }

    @Data
    public static class BroaderOccupation {
        private String conceptType;
        private String conceptUri;
        private String broaderType;
        private String broaderUri;
    }

    @Bean("ProcessBroaderSkills.reader")
    public FlatFileItemReader<BroaderSkill> broaderSkillItemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "broaderType", "broaderUri"};

        return new FlatFileItemReaderBuilder<BroaderSkill>()
                .name("ProcessBroaderSkills Reader")
                .resource(new ClassPathResource("broaderRelationsSkillPillar.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(BroaderSkill.class)
                .maxItemCount(config.getMaxCount())
                .build();
    }

    @Data
    public static class BroaderSkill {
        private String conceptType;
        private String conceptUri;
        private String broaderType;
        private String broaderUri;
    }

    @Bean
    public FlatFileItemReader<SkillSkillRelation> skillSkillRelationItemReader() {
        final String[] fields = new String[]{"originalSkillUri", "originalSkillType", "relationType", "relatedSkillType", "relatedSkillUri"};

        return new FlatFileItemReaderBuilder<SkillSkillRelation>()
                .name("ProcessSkillSkillRelation Reader")
                .resource(new ClassPathResource("skillSkillRelations.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(SkillSkillRelation.class)
                .maxItemCount(config.getMaxCount())
                .build();
    }

    @Data
    public static class SkillSkillRelation {
        private String originalSkillUri;
        private String originalSkillType;
        private String relationType;
        private String relatedSkillType;
        private String relatedSkillUri;
    }

    @Bean("ProcessTransversals.reader")
    public FlatFileItemReader<TransversalInput> transversalInputItemReader() {
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

    @Bean
    public FlatFileItemReader<OccupationalSkillRelation> occupationalSkillRelationItemReader() {
        final String[] fields = new String[]{"occupationUri", "relationType", "skillType", "skillUri"};

        return new FlatFileItemReaderBuilder<OccupationalSkillRelation>()
                .name("ProcessOccupationalSkillRelation Reader")
                .resource(new ClassPathResource("occupationSkillRelations.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .targetType(OccupationalSkillRelation.class)
                .maxItemCount(config.getMaxCount())
                .build();
    }

    @Data
    public static class OccupationalSkillRelation {
        private String occupationUri;
        private String relationType;
        private String skillType;
        private String skillUri;
    }
}
