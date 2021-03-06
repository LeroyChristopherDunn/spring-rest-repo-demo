package org.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// https://spring.io/guides/gs/accessing-data-rest/
// https://www.baeldung.com/spring-rest-openapi-documentation
// https://springdoc.org/
@SpringBootApplication
public class SpringRestRepoDemoApplication {

	private static final Logger log = LoggerFactory.getLogger(SpringRestRepoDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringRestRepoDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner swaggerInfo() {
		return args -> {
			log.info("Open API docs: http://localhost:8080/v3/api-docs/ " );
			log.info("Swagger UI: http://localhost:8080/swagger-ui.html ");
		};
	}
}
