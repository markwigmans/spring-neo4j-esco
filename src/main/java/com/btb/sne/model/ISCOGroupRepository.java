package com.btb.sne.model;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISCOGroupRepository extends Neo4jRepository<ISCOGroup, String> {
}
