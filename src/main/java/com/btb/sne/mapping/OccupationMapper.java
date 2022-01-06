package com.btb.sne.mapping;

import com.btb.sne.model.Occupation;
import com.btb.sne.model.jpa.J_Occupation;
import com.btb.sne.model.neo.N_Occupation;
import org.mapstruct.Mapper;

@Mapper
public interface OccupationMapper {

    N_Occupation toNeo(Occupation occupation);

    J_Occupation toJpa(Occupation occupation);

    Occupation from(N_Occupation occupation);

    Occupation from(J_Occupation occupation);
}
