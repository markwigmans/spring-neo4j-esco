package com.btb.sne.batch;

import com.btb.sne.mapping.ISCOGroupMapper;
import com.btb.sne.mapping.OccupationMapper;
import com.btb.sne.mapping.SkillGroupMapper;
import com.btb.sne.mapping.SkillMapper;
import com.btb.sne.model.ISCOGroup;
import com.btb.sne.model.Occupation;
import com.btb.sne.model.Skill;
import com.btb.sne.model.SkillGroup;
import com.btb.sne.model.jpa.J_ISCOGroup;
import com.btb.sne.model.jpa.J_Occupation;
import com.btb.sne.model.jpa.J_Skill;
import com.btb.sne.model.jpa.J_SkillGroup;
import com.btb.sne.service.J_ISCOGroupService;
import com.btb.sne.service.J_OccupationService;
import com.btb.sne.service.J_SkillGroupService;
import com.btb.sne.service.J_SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JpaWriters {

    @Autowired
    private DataSource dataSource;

    @Bean("jpa.transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager tm = new JpaTransactionManager();
        tm.setDataSource(dataSource);
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }

    private final J_SkillService skillService;
    private final J_SkillGroupService skillGroupService;
    private final J_OccupationService occupationService;
    private final J_ISCOGroupService iscoGroupService;
    private final SkillMapper skillMapper;
    private final SkillGroupMapper skillGroupMapper;
    private final OccupationMapper occupationMapper;
    private final ISCOGroupMapper iscoGroupMapper;

    @Bean("jpa.skill")
    public ItemWriter<Skill> skillItemWriter() {
        return items -> skillService.save(items.stream().map(skillMapper::toJpa).toList());
    }

    @Bean("jpa.skillGroup")
    public ItemWriter<SkillGroup> skillGroupItemWriter() {
        return items -> skillGroupService.save(items.stream().map(skillGroupMapper::toJpa).toList());
    }

    @Bean("jpa.Occupation")
    public ItemWriter<Occupation> occupationItemWriter() {
        return items -> occupationService.save(items.stream().map(occupationMapper::toJpa).toList());
    }

    @Bean("jpa.ISCOGroup")
    public ItemWriter<ISCOGroup> iscoGroupItemWriter() {
        return items -> iscoGroupService.save(items.stream().map(iscoGroupMapper::toJpa).toList());
    }

    @Bean("jpa.TransversalInput")
    public ItemWriter<Readers.TransversalInput> transversalInputSkillItemWriter() {
        return records -> records.forEach(item -> {
            final J_Skill skill = skillService.get(item.getConceptUri()).orElseGet(() -> skillService.save(skillMapper.toJpa(item)));
            String[] items = item.getBroaderConceptUri().split("\\|");
            List<J_Skill> skills = Arrays.stream(items).map(String::trim).map(skillService::get).flatMap(Optional::stream).toList();
            List<J_SkillGroup> skillGroups = Arrays.stream(items).map(String::trim).map(skillGroupService::get).flatMap(Optional::stream).toList();
            skill.getBroaderNodes().addAll(skills);
            skill.getBroaderGroup().addAll(skillGroups);
            skillService.save(skill);
        });
    }

    @Bean("jpa.SkillSkillRelation")
    public ItemWriter<Readers.SkillSkillRelation> skillSkillRelationItemWriter() {
        return items -> {
            for (Readers.SkillSkillRelation item : items) {
                Optional<J_Skill> skill1 = skillService.get(item.getOriginalSkillUri());
                Optional<J_Skill> skill2 = skillService.get(item.getRelatedSkillUri());
                if (skill1.isPresent() && skill2.isPresent()) {
                    switch (item.getRelationType()) {
                        case "optional" -> skill1.get().getOptionalSkills().add(skill2.get());
                        case "essential" -> skill1.get().getEssentialSkills().add(skill2.get());
                        default -> log.warn("unknown type: {}", item.getRelationType());
                    }
                    skillService.save(skill1.get());
                } else {
                    log.warn("{} : {} : {} : {}", item.getOriginalSkillUri(), skill1.isPresent(), item.getRelatedSkillUri(), skill1.isPresent());
                }
            }
        };
    }

    @Bean("jpa.BroaderSkill")
    public ClassifierCompositeItemWriter<Readers.BroaderSkill> broaderSkillItemWriter() {
        Classifier<Readers.BroaderSkill, ItemWriter<? super Readers.BroaderSkill>> classifier =
                new ProcessBroaderSkills.BroaderOccupationClassifier(
                        new SkillGroupSkillGroupWriter(),
                        new SkillSkillGroupWriter(),
                        new SkillSkillWriter());
        return new ClassifierCompositeItemWriterBuilder<Readers.BroaderSkill>().classifier(classifier).build();
    }

    class SkillGroupSkillGroupWriter implements ItemWriter<Readers.BroaderSkill> {

        @Override
        public void write(List<? extends Readers.BroaderSkill> list) {
            for (Readers.BroaderSkill item : list) {
                Optional<J_SkillGroup> grp1 = skillGroupService.get(item.getConceptUri());
                Optional<J_SkillGroup> grp2 = skillGroupService.get(item.getBroaderUri());
                if (grp1.isPresent() && grp2.isPresent()) {
                    if (grp2.get().getBroaderNodes().add(grp1.get())) {
                        skillGroupService.save(grp2.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.getConceptType(), grp1.isPresent(), item.getBroaderUri(), grp2.isPresent());
                }
            }
        }
    }

    class SkillSkillGroupWriter implements ItemWriter<Readers.BroaderSkill> {

        @Override
        public void write(List<? extends Readers.BroaderSkill> list) {
            for (Readers.BroaderSkill item : list) {
                Optional<J_Skill> skill = skillService.get(item.getConceptUri());
                Optional<J_SkillGroup> group = skillGroupService.get(item.getBroaderUri());
                if (skill.isPresent() && group.isPresent()) {
                    if (skill.get().getBroaderGroup().add(group.get())) {
                        skillService.save(skill.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.getConceptUri(), skill.isPresent(), item.getBroaderUri(), group.isPresent());
                }
            }
        }
    }

    class SkillSkillWriter implements ItemWriter<Readers.BroaderSkill> {

        @Override
        public void write(List<? extends Readers.BroaderSkill> list) {
            for (Readers.BroaderSkill item : list) {
                Optional<J_Skill> skill1 = skillService.get(item.getConceptUri());
                Optional<J_Skill> skill2 = skillService.get(item.getBroaderUri());
                if (skill1.isPresent() && skill2.isPresent()) {
                    if (skill1.get().getBroaderNodes().add(skill2.get())) {
                        skillService.save(skill1.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.getConceptUri(), skill1.isPresent(), item.getBroaderUri(), skill2.isPresent());
                }
            }
        }
    }

    @Bean("jpa.BroaderOccupation")
    public ClassifierCompositeItemWriter<Readers.BroaderOccupation> broaderOccupationItemWriter() {
        Classifier<Readers.BroaderOccupation, ItemWriter<? super Readers.BroaderOccupation>> classifier =
                new ProcessBroaderOccupations.BroaderOccupationClassifier(
                        new GroupGroupWriter(),
                        new OccupationGroupWriter(),
                        new OccupationOccupationWriter());
        return new ClassifierCompositeItemWriterBuilder<Readers.BroaderOccupation>().classifier(classifier).build();
    }

    class GroupGroupWriter implements ItemWriter<Readers.BroaderOccupation> {

        @Override
        public void write(List<? extends Readers.BroaderOccupation> list) {
            for (Readers.BroaderOccupation item : list) {
                Optional<J_ISCOGroup> grp1 = iscoGroupService.get(item.getConceptUri());
                Optional<J_ISCOGroup> grp2 = iscoGroupService.get(item.getBroaderUri());
                if (grp1.isPresent() && grp2.isPresent()) {
                    if (grp2.get().getBroaderNodes().add(grp1.get())) {
                        iscoGroupService.save(grp2.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.getConceptUri(), grp1.isPresent(), item.getBroaderUri(), grp2.isPresent());
                }
            }
        }
    }

    class OccupationGroupWriter implements ItemWriter<Readers.BroaderOccupation> {

        @Override
        public void write(List<? extends Readers.BroaderOccupation> list) {
            for (Readers.BroaderOccupation item : list) {
                Optional<J_Occupation> occupation = occupationService.get(item.getConceptUri());
                Optional<J_ISCOGroup> group = iscoGroupService.get(item.getBroaderUri());
                if (occupation.isPresent() && group.isPresent()) {
                    if (occupation.get().getBroaderGroup().add(group.get())) {
                        occupationService.save(occupation.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.getConceptUri(), occupation.isPresent(), item.getBroaderUri(), group.isPresent());
                }
            }
        }
    }

    class OccupationOccupationWriter implements ItemWriter<Readers.BroaderOccupation> {

        @Override
        public void write(List<? extends Readers.BroaderOccupation> list) {
            for (Readers.BroaderOccupation item : list) {
                Optional<J_Occupation> occu1 = occupationService.get(item.getConceptUri());
                Optional<J_Occupation> occu2 = occupationService.get(item.getBroaderUri());
                if (occu1.isPresent() && occu2.isPresent()) {
                    if (occu2.get().getBroaderNodes().add(occu1.get())) {
                        occupationService.save(occu2.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.getConceptUri(), occu1.isPresent(), item.getBroaderUri(), occu1.isPresent());
                }
            }
        }
    }

    @Bean("jpa.OccupationalSkillRelation")
    public ItemWriter<Readers.OccupationalSkillRelation> occupationalSkillRelationItemWriter() {
        return items -> {
            // group By occupation
            Map<String, ? extends List<? extends Readers.OccupationalSkillRelation>> collect =
                    items.stream().collect(groupingBy(Readers.OccupationalSkillRelation::getOccupationUri));

            collect.forEach((key, value) -> {
                final Set<J_Skill> optionals = new HashSet<>();
                final Set<J_Skill> essentials = new HashSet<>();
                Optional<J_Occupation> occupation = occupationService.get(key);
                if (occupation.isPresent()) {
                    value.forEach(s -> {
                        Optional<J_Skill> skill = skillService.get(s.getSkillUri());
                        if (skill.isPresent()) {
                            switch (s.getRelationType()) {
                                case "optional" -> optionals.add(skill.get());
                                case "essential" -> essentials.add(skill.get());
                                default -> log.warn("unknown type: {}", s.getRelationType());
                            }
                        } else {
                            log.warn("Skill {} does not exist ", s.getSkillUri());
                        }
                    });
                    // if value creates a change
                    if (occupation.get().getOptionalSkills().addAll(optionals) &&
                            occupation.get().getEssentialSkills().addAll(essentials)) {
                        occupationService.save(occupation.get());
                    }
                } else {
                    log.warn("Occupation {} does not exist ", key);
                }
            });
        };
    }
}
