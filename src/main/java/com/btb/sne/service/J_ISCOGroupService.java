package com.btb.sne.service;

import com.btb.sne.model.jpa.J_ISCOGroup;
import com.btb.sne.model.jpa.J_ISCOGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class J_ISCOGroupService {

    private final J_ISCOGroupRepository repository;

    public J_ISCOGroup save(J_ISCOGroup group) {
        return repository.save(group);
    }

    public void save(List<? extends J_ISCOGroup> group) {
        repository.saveAll(group);
    }

    public Optional<J_ISCOGroup> get(String uri) {
        return repository.findById(uri);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
