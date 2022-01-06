package com.btb.sne.model.neo;

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
@Node("Occupation")
public class N_Occupation extends N_BaseEntity {

    private String conceptType;
    private String iscoGroup;
    private String preferredLabel;
    private String altLabels;
    private String hiddenLabels;
    private String status;
    private String modifiedDate;
    private String regulatedProfessionNote;
    private String scopeNote;
    private String definition;
    private String inScheme;
    private String description;
    private String code;

    @ToString.Exclude
    @Relationship(type = "PART_OF")
    private Set<N_ISCOGroup> broaderGroup = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = "BROADER_THAN")
    private Set<N_Occupation> broaderNodes = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = "ESSENTIAL_FOR")
    private Set<N_Skill> essentialSkills = new HashSet<>();

    @ToString.Exclude
    @Relationship(type = "OPTIONAL_FOR")
    private Set<N_Skill> optionalSkills = new HashSet<>();
}

