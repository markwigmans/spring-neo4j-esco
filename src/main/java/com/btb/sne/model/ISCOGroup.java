package com.btb.sne.model;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node
public class ISCOGroup {
    private String conceptType;
    @Id
    private String conceptUri;
    private String code;
    private String preferredLabel;
    private String altLabels;
    private String inScheme;
    private String description;

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private List<ISCOGroup> broaderNodes = new ArrayList<>();
}
