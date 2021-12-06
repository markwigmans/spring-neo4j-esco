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
public class Occupation extends BaseEntity {

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

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private Set<ISCOGroup> broaderGroup = new HashSet<>();

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private Set<Occupation> broaderNodes = new HashSet<>();
}

