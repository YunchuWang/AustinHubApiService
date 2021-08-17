package com.austinhub.apiservice.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfig {

      @Bean
      public Counter visitCounter(MeterRegistry meterRegistry)  {
          return Counter.builder("visit_counter")
                        .description("Number of visits to the site")
                        .register(meterRegistry);
      }
}
