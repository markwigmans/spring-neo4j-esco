package com.btb.sne.service;

import com.btb.sne.mapping.MapperUtils;
import com.btb.sne.mapping.OccupationMapper;
import com.btb.sne.model.Occupation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OccupationService {

    private final N_OccupationService neoService;
    private final J_OccupationService jpaService;
    private final OccupationMapper mapper;

    public Occupation save(Occupation entity, RepoType type) {
        return switch (type) {
            case NEO -> mapper.from(neoService.save(mapper.toNeo(entity)));
            case JPA -> mapper.from(jpaService.save(mapper.toJpa(entity)));
        };
    }

    public void save(List<? extends Occupation> entities, RepoType type) {
        switch (type) {
            case NEO -> neoService.save(entities.stream().map(mapper::toNeo).toList());
            case JPA -> jpaService.save(entities.stream().map(mapper::toJpa).toList());
        }
    }

    public Optional<Occupation> get(String uri, RepoType type) {
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
