package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Occupation")
@Getter
@Setter
@ToString
public class J_Occupation extends J_BaseEntity {

    private String conceptType;
    private String iscoGroup;
    private String preferredLabel;
    @Lob
    private String altLabels;
    @Lob
    private String hiddenLabels;
    private String status;
    private String modifiedDate;
    private String regulatedProfessionNote;
    private String scopeNote;
    private String definition;
    private String inScheme;
    @Lob
    private String description;
    private String code;

    @ManyToMany
    @ToString.Exclude
    private Set<J_ISCOGroup> broaderGroup = new HashSet<>();

    @ManyToMany
    @ToString.Exclude
    private Set<J_Occupation> broaderNodes = new HashSet<>();

    @ManyToMany
    @ToString.Exclude
    private Set<J_Skill> essentialSkills = new HashSet<>();

    @ManyToMany
    @ToString.Exclude
    private Set<J_Skill> optionalSkills = new HashSet<>();
}

