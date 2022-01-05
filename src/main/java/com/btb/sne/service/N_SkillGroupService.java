package com.btb.sne.service;

import com.btb.sne.model.neo.N_SkillGroup;
import com.btb.sne.model.neo.N_SkillGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class N_SkillGroupService {

    private final N_SkillGroupRepository repository;

    public void save(N_SkillGroup skillGroup) {
        repository.save(skillGroup);
    }

    public void save(List<? extends N_SkillGroup> skillGroups) {
        repository.saveAll(skillGroups);
    }

    public Optional<N_SkillGroup> get(String uri) {
        return repository.findById(uri);
    }
}
