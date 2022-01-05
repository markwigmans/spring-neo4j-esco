package com.btb.sne.model;

import lombok.Data;

@Data
public class ISCOGroup  {

    private String conceptUri;
    private String conceptType;
    private String code;
    private String preferredLabel;
    private String altLabels;
    private String inScheme;
    private String description;
}
