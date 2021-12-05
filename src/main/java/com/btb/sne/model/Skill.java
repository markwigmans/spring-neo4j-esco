package com.btb.sne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node
public class Skill {

    private String conceptType;
    @Id
    private String conceptUri;
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
    private List<SkillGroup> broaderGroup = new ArrayList<>();

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private List<Skill> broaderNodes = new ArrayList<>();
}

