package com.btb.sne.service;

import com.btb.sne.model.ISCOGroup;
import com.btb.sne.model.ISCOGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ISCOGroupService {

    private final ISCOGroupRepository repository;

    public void save(ISCOGroup group) {
        repository.save(group);
    }

    public void save(List<? extends ISCOGroup> group) {
        repository.saveAll(group);
    }

    public Optional<ISCOGroup> get(String uri) {
        return repository.findById(uri);
    }
}
