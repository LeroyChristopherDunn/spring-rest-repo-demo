package org.example.demo;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/people")
public class PersonController {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PagedResourcesAssembler<Person> pagedResourcesAssembler;

    @Autowired
    private PersonModelAssembler personModelAssembler;

    @GetMapping(value = "/search")
    PagedModel<EntityModel<Person>> searchPeople(@ParameterObject Pageable pageable, @ParameterObject PersonQuery query) {
        Specification<Person> specification = PersonSpecifications.getSpecification(query);
        Page<Person> people = repository.findAll(specification, pageable);
        return pagedResourcesAssembler.toModel(people, personModelAssembler);
    }
}

