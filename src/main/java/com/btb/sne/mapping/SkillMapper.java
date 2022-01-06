package com.btb.sne.mapping;

import com.btb.sne.batch.Readers;
import com.btb.sne.model.Skill;
import com.btb.sne.model.jpa.J_Skill;
import com.btb.sne.model.neo.N_Skill;
import org.mapstruct.Mapper;

@Mapper
public interface SkillMapper {

    N_Skill toNeo(Skill skill);

    J_Skill toJpa(Skill skill);

    Skill from(N_Skill skill);

    Skill from(J_Skill skill);

    N_Skill toNeo(Readers.TransversalInput input);

    J_Skill toJpa(Readers.TransversalInput input);
}
