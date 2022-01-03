package com.btb.sne.service;

import com.btb.sne.model.Skill;
import com.btb.sne.model.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final String CACHE = "skills";

    private final SkillRepository repository;

    @CachePut(CACHE)
    public void save(Skill skill) {
        repository.save(skill);
    }

    public void save(List<? extends Skill> skills) {
        repository.saveAll(skills);
    }

    @Cacheable(value = CACHE)
    public Optional<Skill> get(String uri) {
        return repository.findById(uri);
    }
}
