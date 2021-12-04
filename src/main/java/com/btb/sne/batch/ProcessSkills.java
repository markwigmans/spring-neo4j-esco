package com.btb.sne.batch;

import com.btb.sne.data.Skill;
import com.btb.sne.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class ProcessSkills {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;
    public final SkillService service;

    @Bean
    public Step skillsStep() {
        return this.stepBuilderFactory.get("Skills")
                .<Skill,Skill>chunk(100)
                .reader(skillsReader())
                .writer(skillsWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<Skill> skillsReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "skillType", "reuseLevel", "preferredLabel", "altLabels", "hiddenLabels", "status", "modifiedDate", "scopeNote", "definition", "inScheme", "description"};

        return new FlatFileItemReaderBuilder<Skill>()
                .name("skillsReader")
                .resource(new ClassPathResource("skills_nl.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Skill.class);
                }})
                .targetType(Skill.class)
                .build();

    }

    @Bean
    public ItemWriter<Skill> skillsWriter() {
        return service::save;
    }
}
