package com.btb.sne.mapping;

import com.btb.sne.model.ISCOGroup;
import com.btb.sne.model.jpa.J_ISCOGroup;
import com.btb.sne.model.neo.N_ISCOGroup;
import org.mapstruct.Mapper;

@Mapper
public interface ISCOGroupMapper {

    N_ISCOGroup toNeo(ISCOGroup iscoGroup);
    J_ISCOGroup toJpa(ISCOGroup iscoGroup);
    ISCOGroup from(N_ISCOGroup iscoGroup);
    ISCOGroup from(J_ISCOGroup iscoGroup);
}
