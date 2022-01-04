package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import com.btb.sne.model.ISCOGroup;
import com.btb.sne.model.Occupation;
import com.btb.sne.service.ISCOGroupService;
import com.btb.sne.service.OccupationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessBroaderOccupations {

    private static final String ISCO_GROUP_TYPE = "ISCOGroup";

    private final StepBuilderFactory stepBuilderFactory;
    private final ISCOGroupService iscoGroupService;
    private final OccupationService occupationService;
    private final ApplicationConfig config;

    @Bean("ProcessBroaderOccupations.step")
    public Step step() {
        return this.stepBuilderFactory.get("Broader Occupation relations")
                .<BroaderOccupation, BroaderOccupation>chunk(config.getChunkSize())
                .reader(itemReader())
                .writer(itemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessBroaderOccupations.reader")
    @StepScope
    public FlatFileItemReader<BroaderOccupation> itemReader() {
        final String[] fields = new String[]{"conceptType", "conceptUri", "broaderType", "broaderUri"};

        return new FlatFileItemReaderBuilder<BroaderOccupation>()
                .name("ProcessBroaderOccupations Reader")
                .resource(new ClassPathResource("broaderRelationsOccPillar.csv"))
                .linesToSkip(1) // skip header
                .recordSeparatorPolicy(new SeparatorPolicy(fields.length))
                .delimited()
                .names(fields)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(BroaderOccupation.class);
                }})
                .targetType(BroaderOccupation.class)
                .build();
    }

    @Bean("ProcessBroaderOccupations.writer")
    public ClassifierCompositeItemWriter<BroaderOccupation> itemWriter() {
        Classifier<BroaderOccupation, ItemWriter<? super BroaderOccupation>> classifier =
                new BroaderOccupationClassifier(new GroupGroupWriter(), new OccupationGroupWriter(), new OccupationOccupationWriter());
        return new ClassifierCompositeItemWriterBuilder<BroaderOccupation>().classifier(classifier).build();
    }

    @Data
    public static class BroaderOccupation {
        private String conceptType;
        private String conceptUri;
        private String broaderType;
        private String broaderUri;
    }

    public static class BroaderOccupationClassifier implements Classifier<BroaderOccupation, ItemWriter<? super BroaderOccupation>> {
        private final ItemWriter<BroaderOccupation> groupGroupWriter;
        private final ItemWriter<BroaderOccupation> occupGroupWriter;
        private final ItemWriter<BroaderOccupation> occupOccupWriter;

        public BroaderOccupationClassifier(ItemWriter<BroaderOccupation> w1, ItemWriter<BroaderOccupation> w2, ItemWriter<BroaderOccupation> w3) {
            this.groupGroupWriter = w1;
            this.occupGroupWriter = w2;
            this.occupOccupWriter = w3;
        }


        @Override
        public ItemWriter<? super BroaderOccupation> classify(BroaderOccupation bo) {
            if (bo.conceptType.equals(ISCO_GROUP_TYPE)) {
                return groupGroupWriter;
            } else if (bo.broaderType.equals(ISCO_GROUP_TYPE)) {
                return occupGroupWriter;
            } else {
                return occupOccupWriter;
            }
        }
    }

    class GroupGroupWriter implements ItemWriter<BroaderOccupation> {

        @Override
        public void write(List<? extends BroaderOccupation> list) {
            for (BroaderOccupation item : list) {
                Optional<ISCOGroup> grp1 = iscoGroupService.get(item.conceptUri);
                Optional<ISCOGroup> grp2 = iscoGroupService.get(item.broaderUri);
                if (grp1.isPresent() && grp2.isPresent()) {
                    if (grp1.get().getBroaderNodes().add(grp2.get())) {
                        iscoGroupService.save(grp1.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.conceptUri, grp1.isPresent(), item.broaderUri, grp2.isPresent());
                }
            }
        }
    }

    class OccupationGroupWriter implements ItemWriter<BroaderOccupation> {

        @Override
        public void write(List<? extends BroaderOccupation> list) {
            for (BroaderOccupation item : list) {
                Optional<Occupation> occupation = occupationService.get(item.conceptUri);
                Optional<ISCOGroup> group = iscoGroupService.get(item.broaderUri);
                if (occupation.isPresent() && group.isPresent()) {
                    if (occupation.get().getBroaderGroup().add(group.get())) {
                        occupationService.save(occupation.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.conceptUri, occupation.isPresent(), item.broaderUri, group.isPresent());
                }
            }
        }
    }

    class OccupationOccupationWriter implements ItemWriter<BroaderOccupation> {

        @Override
        public void write(List<? extends BroaderOccupation> list) {
            for (BroaderOccupation item : list) {
                Optional<Occupation> occu1 = occupationService.get(item.conceptUri);
                Optional<Occupation> occu2 = occupationService.get(item.broaderUri);
                if (occu1.isPresent() && occu2.isPresent()) {
                    if (occu1.get().getBroaderNodes().add(occu2.get())) {
                        occupationService.save(occu1.get());
                    }
                } else {
                    log.warn("{} : {} : {} : {}", item.conceptUri, occu1.isPresent(), item.broaderUri, occu2.isPresent());
                }
            }
        }
    }
}
