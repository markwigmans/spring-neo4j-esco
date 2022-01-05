package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class J_Skill extends J_BaseEntity {

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

    @ToString.Exclude
    private Set<J_SkillGroup> broaderGroup = new HashSet<>();

    @ToString.Exclude
    private Set<J_Skill> broaderNodes = new HashSet<>();

    @ToString.Exclude
    private Set<J_Skill> essentialSkills = new HashSet<>();

    @ToString.Exclude
    private Set<J_Skill> optionalSkills = new HashSet<>();
}


