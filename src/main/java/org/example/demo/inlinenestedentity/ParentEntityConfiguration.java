package org.example.demo.inlinenestedentity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@SuppressWarnings("unused")
@Configuration
public class ParentEntityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ParentEntityConfiguration.class);

    @Bean
    public RepositoryRestConfigurer parentEntityRepositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> config.exposeIdsFor(ParentEntity.class));
    }
}
