package com.btb.sne.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ISCOGroup {

    private String conceptUri;
    private String conceptType;
    private String code;
    private String preferredLabel;
    private String altLabels;
    private String inScheme;
    private String description;
}
