package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class J_SkillGroup extends J_BaseEntity {

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

    @ToString.Exclude
    private Set<J_SkillGroup> broaderNodes = new HashSet<>();
}

