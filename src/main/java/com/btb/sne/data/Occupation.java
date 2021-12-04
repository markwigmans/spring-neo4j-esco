package com.btb.sne.data;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node
public class Occupation {

    private String conceptType;
    @Id
    private String conceptUri;
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
}
