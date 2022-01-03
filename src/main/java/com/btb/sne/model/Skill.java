package com.btb.sne.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Node
public class Skill extends BaseEntity {

    private String conceptType;
    private String skillType;
    private String reuseLevel;
    private String preferredLabel;
    private String altLabels;
    private String hiddenLabels;
    private String status;
    private String modifiedDate;
    private String scopeNote;
    private String definition;
    private String inScheme;
    private String description;

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private Set<SkillGroup> broaderGroup = new HashSet<>();

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private Set<Skill> broaderNodes = new HashSet<>();

    @Relationship(type = "ESSENTIAL_FOR", direction = Relationship.Direction.INCOMING)
    private Set<Skill> essentialSkills = new HashSet<>();

    @Relationship(type = "OPTIONAL_FOR", direction = Relationship.Direction.INCOMING)
    private Set<Skill> optionalSkills = new HashSet<>();
}


