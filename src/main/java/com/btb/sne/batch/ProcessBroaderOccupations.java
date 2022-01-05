package com.btb.sne.batch;

import com.btb.sne.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessBroaderOccupations {

    private static final String ISCO_GROUP_TYPE = "ISCOGroup";

    private final StepBuilderFactory stepBuilderFactory;
    private final ApplicationConfig config;
    private final Readers readers;
    private final NeoWriters neoWriters;
    private final JpaWriters jpaWriters;

    @Bean("ProcessBroaderOccupations.neo.step")
    public Step neoStep() {
        return this.stepBuilderFactory.get("Broader Occupation relations")
                .<Readers.BroaderOccupation, Readers.BroaderOccupation>chunk(config.getChunkSize())
                .reader(readers.broaderOccupationItemReader())
                .writer(neoWriters.broaderOccupationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }

    @Bean("ProcessBroaderOccupations.jpa.step")
    public Step jpaStep() {
        return this.stepBuilderFactory.get("Broader Occupation relations")
                .<Readers.BroaderOccupation, Readers.BroaderOccupation>chunk(config.getChunkSize())
                .reader(readers.broaderOccupationItemReader())
                .writer(jpaWriters.broaderOccupationItemWriter())
                .listener(new StepChunkListener())
                .build();
    }


    public static class BroaderOccupationClassifier implements Classifier<Readers.BroaderOccupation,
            ItemWriter<? super Readers.BroaderOccupation>> {
        private final ItemWriter<Readers.BroaderOccupation> groupGroupWriter;
        private final ItemWriter<Readers.BroaderOccupation> occupGroupWriter;
        private final ItemWriter<Readers.BroaderOccupation> occupOccupWriter;

        public BroaderOccupationClassifier(ItemWriter<Readers.BroaderOccupation> w1,
                                           ItemWriter<Readers.BroaderOccupation> w2,
                                           ItemWriter<Readers.BroaderOccupation> w3) {
            this.groupGroupWriter = w1;
            this.occupGroupWriter = w2;
            this.occupOccupWriter = w3;
        }

        @Override
        public ItemWriter<? super Readers.BroaderOccupation> classify(Readers.BroaderOccupation bo) {
            if (bo.getConceptType().equals(ISCO_GROUP_TYPE)) {
                return groupGroupWriter;
            } else if (bo.getBroaderType().equals(ISCO_GROUP_TYPE)) {
                return occupGroupWriter;
            } else {
                return occupOccupWriter;
            }
        }
    }
}
