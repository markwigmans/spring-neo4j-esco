package com.btb.sne.service;

import com.btb.sne.mapping.MapperUtils;
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
    private final SkillMapper mapper;

    public Skill save(Skill entity, RepoType type) {
        return switch (type) {
            case NEO -> mapper.from(neoService.save(mapper.toNeo(entity)));
            case JPA -> mapper.from(jpaService.save(mapper.toJpa(entity)));
        };
    }

    public void save(List<? extends Skill> entities, RepoType type) {
        switch (type) {
            case NEO -> neoService.save(entities.stream().map(mapper::toNeo).toList());
            case JPA -> jpaService.save(entities.stream().map(mapper::toJpa).toList());
        }
    }

    public Optional<Skill> get(String uri, RepoType type) {
        return switch (type) {
            case NEO -> MapperUtils.wrap(mapper.from(MapperUtils.unwrap(neoService.get(uri))));
            case JPA -> MapperUtils.wrap(mapper.from(MapperUtils.unwrap(jpaService.get(uri))));
        };
    }

    public void deleteAll(RepoType type) {
        switch (type) {
            case NEO -> neoService.deleteAll();
            case JPA -> jpaService.deleteAll();
        }
    }
}
