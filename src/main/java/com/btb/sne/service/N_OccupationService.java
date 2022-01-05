package com.btb.sne.service;

import com.btb.sne.model.neo.N_Occupation;
import com.btb.sne.model.neo.N_OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class N_OccupationService {

    private final N_OccupationRepository repository;

    public void save(N_Occupation occupation) {
        repository.save(occupation);
    }

    public void save(List<? extends N_Occupation> occupations) {
        repository.saveAll(occupations);
    }

    public Optional<N_Occupation> get(String uri) {
        return repository.findById(uri);
    }
}
