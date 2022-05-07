package org.example.demo.dynamicquery.query;

import org.example.demo.dynamicquery.DynamicQueryEntity;
import org.example.demo.dynamicquery.DynamicQueryRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class DynamicQueryModelAssembler implements RepresentationModelAssembler<DynamicQueryEntity, EntityModel<DynamicQueryEntity>> {

    @Override
    public EntityModel<DynamicQueryEntity> toModel(DynamicQueryEntity entity) {

        return EntityModel.of(entity,
                linkTo(DynamicQueryRepository.class).slash("dynamicquery").slash(entity.getId()).withSelfRel(),
                linkTo(DynamicQueryRepository.class).slash("dynamicquery").slash(entity.getId()).withRel("dynamicquery")
        );
    }
}
