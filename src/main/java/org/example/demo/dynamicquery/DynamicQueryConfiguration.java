package org.example.demo.dynamicquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class DynamicQueryConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DynamicQueryConfiguration.class);

    @Bean
    public RepositoryRestConfigurer dynamicQueryRepositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> config.exposeIdsFor(DynamicQueryEntity.class));
    }
}
