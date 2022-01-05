package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class J_ISCOGroup extends J_BaseEntity {
    private String conceptType;
    private String code;
    private String preferredLabel;
    private String altLabels;
    private String inScheme;
    private String description;

    @ToString.Exclude
    private Set<J_ISCOGroup> broaderNodes = new HashSet<>();
}
