package com.btb.sne.service;

import com.btb.sne.data.Skill;
import com.btb.sne.data.SkillGroup;
import com.btb.sne.data.SkillGroupRepository;
import com.btb.sne.data.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillGroupService {

    private final SkillGroupRepository repository;

    public void save(SkillGroup skillGroup) {
        repository.save(skillGroup);
    }

    public void save(List<? extends SkillGroup> skillGroups) {
        repository.saveAll(skillGroups);
    }
}
