package com.btb.sne.model;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
@ToString
@NoArgsConstructor
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

    @ToString.Exclude
    @Relationship(type = "PART_OF")
    private Set<SkillGroup> broaderGroup = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private Set<Skill> broaderNodes = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = "ESSENTIAL_FOR")
    private Set<Skill> essentialSkills = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = "OPTIONAL_FOR")
    private Set<Skill> optionalSkills = new HashSet<>();

    @Builder
    public Skill(String conceptUri, String conceptType, String skillType, String reuseLevel, String preferredLabel, String altLabels, String hiddenLabels, String status, String modifiedDate, String scopeNote, String definition, String inScheme, String description, Set<SkillGroup> broaderGroup, Set<Skill> broaderNodes, Set<Skill> essentialSkills, Set<Skill> optionalSkills) {
        super(conceptUri);
        this.conceptType = conceptType;
        this.skillType = skillType;
        this.reuseLevel = reuseLevel;
        this.preferredLabel = preferredLabel;
        this.altLabels = altLabels;
        this.hiddenLabels = hiddenLabels;
        this.status = status;
        this.modifiedDate = modifiedDate;
        this.scopeNote = scopeNote;
        this.definition = definition;
        this.inScheme = inScheme;
        this.description = description;
        this.broaderGroup = broaderGroup;
        this.broaderNodes = broaderNodes;
        this.essentialSkills = essentialSkills;
        this.optionalSkills = optionalSkills;
    }
}


