package com.btb.sne.service;

import com.btb.sne.data.Skill;
import com.btb.sne.data.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
