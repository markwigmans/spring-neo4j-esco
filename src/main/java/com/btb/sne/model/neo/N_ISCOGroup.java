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
@Node("ISCOGroup")
public class N_ISCOGroup extends N_BaseEntity {
    private String conceptType;
    private String code;
    private String preferredLabel;
    private String altLabels;
    private String inScheme;
    private String description;

    @ToString.Exclude
    @Relationship(type = "BROADER_THAN")
    private Set<N_ISCOGroup> broaderNodes = new HashSet<>();
}
