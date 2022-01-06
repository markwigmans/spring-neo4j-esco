package com.btb.sne.batch;

import com.btb.sne.service.ISCOGroupService;
import com.btb.sne.service.OccupationService;
import com.btb.sne.service.SkillGroupService;
import com.btb.sne.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.btb.sne.service.RepoType.JPA;
import static com.btb.sne.service.RepoType.NEO;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitService {

    final private SkillService skillService;
    final private OccupationService occupationService;
    final private SkillGroupService skillGroupService;
    final private ISCOGroupService iscoGroupService;

    public void deleteAll() {
        skillService.deleteAll(NEO);
        skillService.deleteAll(JPA);
        occupationService.deleteAll(NEO);
        occupationService.deleteAll(JPA);
        skillGroupService.deleteAll(NEO);
        skillGroupService.deleteAll(JPA);
        iscoGroupService.deleteAll(NEO);
        iscoGroupService.deleteAll(JPA);
    }
}
