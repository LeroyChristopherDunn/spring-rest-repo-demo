package org.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class PersonConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PersonConfiguration.class);

    @Bean
    @Profile("dev")
    CommandLineRunner initDatabaseWithPeople(PersonRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Person("Bilbo", "Baggins")));
            log.info("Preloading " + repository.save(new Person("Frodo", "Baggins")));
        };
    }

    @Bean
    public RepositoryRestConfigurer personRepositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> config.exposeIdsFor(Person.class));
    }
}
