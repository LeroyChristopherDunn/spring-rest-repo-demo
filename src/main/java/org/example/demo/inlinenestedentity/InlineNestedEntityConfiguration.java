package org.example.demo.inlinenestedentity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@SuppressWarnings("unused")
@Configuration
public class InlineNestedEntityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(InlineNestedEntityConfiguration.class);

    @Bean
    public RepositoryRestConfigurer inlineNestedEntityRepositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> config.exposeIdsFor(InlineNestedEntity.class));
    }
}
