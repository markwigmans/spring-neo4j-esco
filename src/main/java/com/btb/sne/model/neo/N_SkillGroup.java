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
@Node("SkillGroup")
public class N_SkillGroup extends N_BaseEntity {

    private String conceptType;
    private String preferredLabel;
    private String altLabels;
    private String hiddenLabels;
    private String status;
    private String modifiedDate;
    private String scopeNote;
    private String inScheme;
    private String description;
    private String code;

    @ToString.Exclude
    @Relationship(type = "BROADER_THAN")
    private Set<N_SkillGroup> broaderNodes = new HashSet<>();
}

