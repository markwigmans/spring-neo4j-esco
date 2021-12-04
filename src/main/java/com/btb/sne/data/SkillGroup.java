package com.btb.sne.data;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

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
}
