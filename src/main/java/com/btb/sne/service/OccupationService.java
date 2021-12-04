package com.btb.sne.service;

import com.btb.sne.data.Occupation;
import com.btb.sne.data.OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
