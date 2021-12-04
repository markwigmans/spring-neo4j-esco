package com.btb.sne.data;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

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

}
