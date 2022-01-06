package com.btb.sne.model.jpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ISCOGroup")
@Getter
@Setter
@ToString
public class J_ISCOGroup extends J_BaseEntity {
    private String conceptType;
    private String code;
    private String preferredLabel;
    @Lob
    private String altLabels;
    private String inScheme;
    @Lob
    private String description;

    @ManyToMany
    @ToString.Exclude
    private Set<J_ISCOGroup> broaderNodes = new HashSet<>();
}
