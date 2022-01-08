# Getting Started

Load [ESCO](https://ec.europa.eu/esco/portal/) into RDBMS and after that into Neo4j.
The framework [Spring Batch](https://spring.io/projects/spring-batch) is used for the transformation.

All CSV files are read into a Neo$j database, then the steps are repeated but then the output is sent to
a RDBMS database.

The is to test Spring Batch and Neo4J performance (compared to an RDBMS).

# CSV Files

| File                               | Class                            |
|------------------------------------|----------------------------------|
| broaderRelationsOccPillar.csv      | ProcessBroaderOccupations        |
| broaderRelationsSkillPillar.csv    | ProcessBroaderSkills             |
| ictSkillsCollection_nl.csv         | ProcessTransversals              |
| ISCOGroups_nl.csv                  | ProcessISCOGroups                |
| languageSkillsCollection_nl.csv    | ProcessTransversals              |
| occupations_nl.csv                 | ProcessOccupations               |
| occupationSkillRelations.csv       | ProcessOccupationalSkillRelation |
| skillGroups_nl.csv                 | ProcessSkillGroups               |
| skills_nl.csv                      | ProcessSkills                    |
| skillsHierarchy_nl.csv             | -                                |
| skillSkillRelations.csv            | ProcessSkillSkillRelation        |
| transversalSkillsCollection_nl.csv | ProcessTransversals              |

# To Do

- [x] broaderRelationsOccPillar.csv
- [x] broaderRelationsSkillPillar.csv
- [x] ictSkillsCollection_nl.csv
- [x] ISCOGroups_nl.csv
- [x] languageSkillsCollection_nl.csv
- [x] occupations_nl.csv
- [x] occupationSkillRelations.csv
- [x] skillGroups_nl.csv
- [x] skills_nl.csv
- [ ] skillsHierarchy_nl.csv
- [x] skillSkillRelations.csv
- [x] transversalSkillsCollection_nl.csv
- [x] Incoming/outgoing relations
- [x] write to JPA database as well

# Usefull Neo4j commands

## delete all

``MATCH (n) DETACH DELETE n;``