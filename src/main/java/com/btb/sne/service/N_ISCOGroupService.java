package com.btb.sne.service;

import com.btb.sne.model.neo.N_ISCOGroup;
import com.btb.sne.model.neo.N_ISCOGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class N_ISCOGroupService {

    private final N_ISCOGroupRepository repository;

    public void save(N_ISCOGroup group) {
        repository.save(group);
    }

    public void save(List<? extends N_ISCOGroup> group) {
        repository.saveAll(group);
    }

    public Optional<N_ISCOGroup> get(String uri) {
        return repository.findById(uri);
    }
}
