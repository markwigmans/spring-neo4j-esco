package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Skill")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class J_Skill extends J_BaseEntity {

    private String conceptType;
    private String skillType;
    private String reuseLevel;
    private String preferredLabel;
    @Lob
    private String altLabels;
    @Lob
    private String hiddenLabels;
    private String status;
    private String modifiedDate;
    private String scopeNote;
    private String definition;
    private String inScheme;
    @Lob
    private String description;

    @ManyToMany
    @ToString.Exclude
    private Set<J_SkillGroup> broaderGroup = new HashSet<>();

    @ManyToMany
    @ToString.Exclude
    private Set<J_Skill> broaderNodes = new HashSet<>();

    @ManyToMany
    @ToString.Exclude
    private Set<J_Skill> essentialSkills = new HashSet<>();

    @ManyToMany
    @ToString.Exclude
    private Set<J_Skill> optionalSkills = new HashSet<>();
}


