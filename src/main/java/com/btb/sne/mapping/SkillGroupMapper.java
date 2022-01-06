package com.btb.sne.mapping;

import com.btb.sne.model.SkillGroup;
import com.btb.sne.model.jpa.J_SkillGroup;
import com.btb.sne.model.neo.N_SkillGroup;
import org.mapstruct.Mapper;

@Mapper
public interface SkillGroupMapper {

    N_SkillGroup toNeo(SkillGroup skillGroup);

    J_SkillGroup toJpa(SkillGroup skillGroup);

    SkillGroup from(N_SkillGroup skillGroup);

    SkillGroup from(J_SkillGroup skillGroup);
}
