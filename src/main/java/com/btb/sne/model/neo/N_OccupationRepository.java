package com.btb.sne.model.neo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface N_OccupationRepository extends Neo4jRepository<N_Occupation, String> {
}
