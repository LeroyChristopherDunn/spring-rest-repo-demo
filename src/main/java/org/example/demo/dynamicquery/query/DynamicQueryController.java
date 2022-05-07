package org.example.demo.dynamicquery.query;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.demo.dynamicquery.DynamicQueryEntity;
import org.example.demo.dynamicquery.DynamicQueryRepository;
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

@SuppressWarnings("unused")
@Tag(name = "DynamicQuery")
@RestController
@RequestMapping("/dynamicquery")
public class DynamicQueryController {

    @Autowired
    private DynamicQueryRepository repository;

    @Autowired
    private PagedResourcesAssembler<DynamicQueryEntity> pagedResourcesAssembler;

    @Autowired
    private DynamicQueryModelAssembler modelAssembler;

    @GetMapping(value = "/search")
    PagedModel<EntityModel<DynamicQueryEntity>> searchDynamicQueryEntities(@ParameterObject Pageable pageable, @ParameterObject DynamicQueryEntityQuery query) {
        Specification<DynamicQueryEntity> specification = DynamicQuerySpecifications.getSpecification(query);
        Page<DynamicQueryEntity> entities = repository.findAll(specification, pageable);
        return pagedResourcesAssembler.toModel(entities, modelAssembler);
    }
}

