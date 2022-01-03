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
public class ISCOGroup extends BaseEntity {
    private String conceptType;
    private String code;
    private String preferredLabel;
    private String altLabels;
    private String inScheme;
    private String description;

    @Relationship(type = "BROADER_THAN", direction = Relationship.Direction.INCOMING)
    private Set<ISCOGroup> broaderNodes = new HashSet<>();
}
