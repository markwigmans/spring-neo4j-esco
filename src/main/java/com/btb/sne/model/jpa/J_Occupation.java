package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class J_Occupation extends J_BaseEntity {

    private String conceptType;
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

    @ToString.Exclude
    private Set<J_ISCOGroup> broaderGroup = new HashSet<>();

    @ToString.Exclude
    private Set<J_Occupation> broaderNodes = new HashSet<>();

    @ToString.Exclude
    private Set<J_Skill> essentialSkills = new HashSet<>();

    @ToString.Exclude
    private Set<J_Skill> optionalSkills = new HashSet<>();
}

