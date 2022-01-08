package com.btb.sne.config;

import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    LoggingMeterRegistry loggingMeterRegistry() {
        return new LoggingMeterRegistry();
    }
}
