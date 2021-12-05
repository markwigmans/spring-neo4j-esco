package com.btb.sne.model;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillGroupRepository extends Neo4jRepository<SkillGroup, String> {
}
