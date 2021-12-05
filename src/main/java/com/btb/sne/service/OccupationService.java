package com.btb.sne.service;

import com.btb.sne.model.Occupation;
import com.btb.sne.model.OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OccupationService {

    private final OccupationRepository repository;

    public void save(Occupation occupation) {
        repository.save(occupation);
    }

    public void save(List<? extends Occupation> occupations) {
        repository.saveAll(occupations);
    }

    public Optional<Occupation> get(String uri) {
        return repository.findById(uri);
    }
}
