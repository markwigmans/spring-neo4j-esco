package com.btb.sne.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node
public class SkillGroup {

    private String conceptType;
    @Id
    private String conceptUri;
    private String preferredLabel;
    private String altLabels;
    private String hiddenLabels;
    private String status;
    private String modifiedDate;
    private String scopeNote;
    private String inScheme;
    private String description;
    private String code;

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private List<SkillGroup> broaderNodes = new ArrayList<>();
}
