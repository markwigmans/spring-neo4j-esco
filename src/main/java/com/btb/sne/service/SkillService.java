package com.btb.sne.service;

import com.btb.sne.model.Skill;
import com.btb.sne.model.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository repository;

    public void save(Skill skill) {
        repository.save(skill);
    }

    public void save(List<? extends Skill> skills) {
        repository.saveAll(skills);
    }

    public Optional<Skill> get(String uri) {
        return repository.findById(uri);
    }
}
