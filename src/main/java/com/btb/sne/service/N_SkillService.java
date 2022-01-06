package com.btb.sne.service;

import com.btb.sne.model.neo.N_Skill;
import com.btb.sne.model.neo.N_SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class N_SkillService {

    private final N_SkillRepository repository;

    public N_Skill save(N_Skill skill) {
        return repository.save(skill);
    }

    public void save(List<? extends N_Skill> skills) {
        repository.saveAll(skills);
    }

    public Optional<N_Skill> get(String uri) {
        return repository.findById(uri);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
