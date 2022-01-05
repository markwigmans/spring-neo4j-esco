package com.btb.sne.service;

import com.btb.sne.mapping.SkillMapper;
import com.btb.sne.model.Skill;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final N_SkillService neoService;
    private final J_SkillService jpaService;
    private final SkillMapper skillMapper;

    public Skill save(Skill skill, RepoType type) {
        return switch (type) {
            case NEO -> skillMapper.from(neoService.save(skillMapper.toNeo(skill)));
            case JPA -> skillMapper.from(jpaService.save(skillMapper.toJpa(skill)));
        };
    }

    public void save(List<? extends Skill> skills, RepoType type) {
        switch (type) {
            case NEO -> neoService.save(skills.stream().map(skillMapper::toNeo).toList());
            case JPA -> jpaService.save(skills.stream().map(skillMapper::toJpa).toList());
        }
    }

    public Optional<Skill> get(String uri, RepoType type) {
        return switch (type) {
            case NEO -> skillMapper.fromNeo(neoService.get(uri));
            case JPA -> skillMapper.fromJpa(jpaService.get(uri));
        };
    }
}
