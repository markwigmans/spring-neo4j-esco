package com.btb.sne.service;

import com.btb.sne.model.jpa.J_Occupation;
import com.btb.sne.model.jpa.J_OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class J_OccupationService {

    private final J_OccupationRepository repository;

    public void save(J_Occupation occupation) {
        repository.save(occupation);
    }

    public void save(List<? extends J_Occupation> occupations) {
        repository.saveAll(occupations);
    }

    public Optional<J_Occupation> get(String uri) {
        return repository.findById(uri);
    }
}
