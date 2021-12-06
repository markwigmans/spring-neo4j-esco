// create index
create index IF NOT EXISTS FOR (n:Skill) ON (n.conceptUri);
create index IF NOT EXISTS FOR (n:SkillGroup) ON (n.conceptUri);
create index IF NOT EXISTS FOR (n:ISCOGroup) ON (n.conceptUri);
create index IF NOT EXISTS FOR (n:Occupation) ON (n.conceptUri);

// Delete all
MATCH (n) DETACH DELETE n;