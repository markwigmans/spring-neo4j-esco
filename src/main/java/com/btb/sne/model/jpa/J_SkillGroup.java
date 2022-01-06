package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "SkillGroup")
@Getter
@Setter
@ToString
public class J_SkillGroup extends J_BaseEntity {

    private String conceptType;
    private String preferredLabel;
    @Lob
    private String altLabels;
    @Lob
    private String hiddenLabels;
    private String status;
    private String modifiedDate;
    private String scopeNote;
    private String inScheme;
    @Lob
    private String description;
    private String code;

    @ManyToMany
    @ToString.Exclude
    private Set<J_SkillGroup> broaderNodes = new HashSet<>();
}

