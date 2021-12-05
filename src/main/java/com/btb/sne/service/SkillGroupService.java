package com.btb.sne.service;

import com.btb.sne.model.SkillGroup;
import com.btb.sne.model.SkillGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<SkillGroup> get(String uri) {
        return repository.findById(uri);
    }
}
