package com.btb.sne.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillGroup {

    private String conceptUri;
    private String conceptType;
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

