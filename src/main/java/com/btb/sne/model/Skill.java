package com.btb.sne.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Skill {

    private String conceptUri;
    private String conceptType;
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


