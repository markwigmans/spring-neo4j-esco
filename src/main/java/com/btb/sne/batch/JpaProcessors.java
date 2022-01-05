package com.btb.sne.batch;

import com.btb.sne.mapping.SkillMapper;
import com.btb.sne.model.Skill;
import com.btb.sne.model.jpa.J_Skill;
import com.btb.sne.model.jpa.J_SkillGroup;
import com.btb.sne.service.J_SkillGroupService;
import com.btb.sne.service.J_SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JpaProcessors {

    private final J_SkillService skillService;
    private final J_SkillGroupService skillGroupService;
    private final SkillMapper skillMapper;

    @Bean("jpa.TransversalInput")
    public ItemProcessor<Readers.TransversalInput, Skill> transversalInputItemProcessor() {
        return new ConverterProcessor();
    }

    class ConverterProcessor implements ItemProcessor<Readers.TransversalInput, Skill> {

        @Override
        public Skill process(Readers.TransversalInput item) throws Exception {
            final J_Skill skill = skillService.get(item.getConceptUri()).orElseGet(() -> skillService.save(skillMapper.toJpa(item)));
            String[] items = item.getBroaderConceptUri().split("\\|");
            skill.getBroaderNodes().addAll(getSkills(items));
            skill.getBroaderGroup().addAll(getSkillGroups(items));
            return skillMapper.from(skill);
        }

        List<J_Skill> getSkills(String[] items) {
            return Arrays.stream(items).map(String::trim).map(skillService::get).flatMap(Optional::stream).toList();
        }

        List<J_SkillGroup> getSkillGroups(String[] items) {
            return Arrays.stream(items).map(String::trim).map(skillGroupService::get).flatMap(Optional::stream).toList();
        }
    }
}
