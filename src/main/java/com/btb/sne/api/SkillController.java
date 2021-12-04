package com.btb.sne.api;

import com.btb.sne.service.SkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skill")
@RequiredArgsConstructor
@Slf4j
public class SkillController {
    private final JobLauncher jobLauncher;
    private final SkillService service;
}
