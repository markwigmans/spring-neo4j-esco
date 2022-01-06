package com.btb.sne.service;

import com.btb.sne.model.jpa.J_Skill;
import com.btb.sne.model.jpa.J_SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class J_SkillService {

    private final J_SkillRepository repository;

    public J_Skill save(J_Skill skill) {
        return repository.save(skill);
    }

    public void save(List<J_Skill> skills) {
        repository.saveAll(skills);
    }

    public Optional<J_Skill> get(String uri) {
        return repository.findById(uri);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
