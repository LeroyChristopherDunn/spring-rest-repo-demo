package org.example.demo.validatedentity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class ValidatedEntityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ValidatedEntityConfiguration.class);

    @Bean
    public RepositoryRestConfigurer validatedEntityRepositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> config.exposeIdsFor(ValidatedEntity.class));
    }
}
