package com.btb.sne.service;

import com.btb.sne.model.jpa.J_SkillGroup;
import com.btb.sne.model.jpa.J_SkillGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class J_SkillGroupService {

    private final J_SkillGroupRepository repository;

    public void save(J_SkillGroup skillGroup) {
        repository.save(skillGroup);
    }

    public void save(List<? extends J_SkillGroup> skillGroups) {
        repository.saveAll(skillGroups);
    }

    public Optional<J_SkillGroup> get(String uri) {
        return repository.findById(uri);
    }
}
