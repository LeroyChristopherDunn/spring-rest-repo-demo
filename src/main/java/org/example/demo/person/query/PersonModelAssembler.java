package org.example.demo.person.query;

import org.example.demo.person.Person;
import org.example.demo.person.PersonRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@SuppressWarnings("unused")
@Component
public class PersonModelAssembler implements RepresentationModelAssembler<Person, EntityModel<Person>> {

    @Override
    public EntityModel<Person> toModel(Person person) {

        return EntityModel.of(person,
                linkTo(PersonRepository.class).slash("people").slash(person.getId()).withSelfRel(),
                linkTo(PersonRepository.class).slash("people").slash(person.getId()).withRel("person")
        );
    }
}
