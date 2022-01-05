package com.btb.sne.model.neo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface N_SkillRepository extends Neo4jRepository<N_Skill, String> {
}
