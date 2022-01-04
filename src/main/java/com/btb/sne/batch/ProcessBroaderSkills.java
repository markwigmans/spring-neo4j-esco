package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.Skill;
import com.btb.sne.model.SkillGroup;
import com.btb.sne.service.SkillGroupService;
import com.btb.sne.service.SkillService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessBroaderSkills {

    private static final String SKILL_GROUP_TYPE = "SkillGroup";

    private final StepBuilderFactory stepBuilderFactory;
    private final SkillGroupService skillGroupService;
    private final SkillService skillService;
    private final ApplicationConfig config;

    @Bean("ProcessBroaderSkills.step")
    public Step step() {
        return this.stepBuilderFactory.get("Broader Skill relations")
                .<BroaderSkill, BroaderSkill>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessBroaderSkills.reader")
    @StepScope
    public FlatFileItemReader<BroaderSkill> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "broaderType", "broaderUri"};

        return new FlatFileItemReaderBuilder<BroaderSkill>()
                .name("ProcessBroaderSkills Reader")
                .resource(new ClassPathResource("broaderRelationsSkillPillar.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(BroaderSkill.class);
                }})
                .targetType(BroaderSkill.class)
                .build();
    }

    @Bean("ProcessBroaderSkills.writer")
    public ClassifierCompositeItemWriter<BroaderSkill> itemWriter() {
        Classifier<BroaderSkill, ItemWriter<? super BroaderSkill>> classifier =
                new BroaderOccupationClassifier(new GroupGroupWriter(), new SkillSkillGroupWriter(), new SkillSkillWriter());
        return new ClassifierCompositeItemWriterBuilder<BroaderSkill>().classifier(classifier).build();
    }

    @Data
    public static class BroaderSkill {
        private String conceptType;
        private String conceptUri;
        private String broaderType;
        private String broaderUri;
    }

    public static class BroaderOccupationClassifier implements Classifier<BroaderSkill, ItemWriter<? super BroaderSkill>> {
        private final ItemWriter<BroaderSkill> groupGroupWriter;
        private final ItemWriter<BroaderSkill> skillSkillGroupWriter;
        private final ItemWriter<BroaderSkill> SkillSkillWriter;

        public BroaderOccupationClassifier(ItemWriter<BroaderSkill> w1, ItemWriter<BroaderSkill> w2, ItemWriter<BroaderSkill> w3) {
            this.groupGroupWriter = w1;
            this.skillSkillGroupWriter = w2;
            this.SkillSkillWriter = w3;
        }


        @Override
        public ItemWriter<? super BroaderSkill> classify(BroaderSkill bs) {
            if (bs.conceptType.equals(SKILL_GROUP_TYPE)) {
                return groupGroupWriter;
            } else if (bs.broaderType.equals(SKILL_GROUP_TYPE)) {
                return skillSkillGroupWriter;
            } else {
                return SkillSkillWriter;
            }
        }
    }

    class GroupGroupWriter implements ItemWriter<BroaderSkill> {

        @Override
        public void write(List<? extends BroaderSkill> list) {
            for (BroaderSkill item : list) {
                Optional<SkillGroup> grp1 = skillGroupService.get(item.conceptUri);
                Optional<SkillGroup> grp2 = skillGroupService.get(item.broaderUri);
                if (grp1.isPresent() && grp2.isPresent()) {
                    if (grp1.get().getBroaderNodes().add(grp2.get())) {
                        skillGroupService.save(grp1.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.conceptUri, grp1.isPresent(), item.broaderUri, grp2.isPresent());
                }
            }
        }
    }

    class SkillSkillGroupWriter implements ItemWriter<BroaderSkill> {

        @Override
        public void write(List<? extends BroaderSkill> list) {
            for (BroaderSkill item : list) {
                Optional<Skill> skill = skillService.get(item.conceptUri);
                Optional<SkillGroup> group = skillGroupService.get(item.broaderUri);
                if (skill.isPresent() && group.isPresent()) {
                    if (skill.get().getBroaderGroup().add(group.get())) {
                        skillService.save(skill.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.conceptUri, skill.isPresent(), item.broaderUri, group.isPresent());
                }
            }
        }
    }

    class SkillSkillWriter implements ItemWriter<BroaderSkill> {

        @Override
        public void write(List<? extends BroaderSkill> list) {
            for (BroaderSkill item : list) {
                Optional<Skill> skill1 = skillService.get(item.conceptUri);
                Optional<Skill> skill2 = skillService.get(item.broaderUri);
                if (skill1.isPresent() && skill2.isPresent()) {
                    if (skill1.get().getBroaderNodes().add(skill2.get())) {
                        skillService.save(skill1.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.conceptUri, skill1.isPresent(), item.broaderUri, skill2.isPresent());
                }
            }
        }
    }
}
