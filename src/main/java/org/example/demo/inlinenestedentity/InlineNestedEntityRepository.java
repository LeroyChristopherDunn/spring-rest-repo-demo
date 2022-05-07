package org.example.demo.inlinenestedentity;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@Tag(name = "InlineNestedEntities")
@RepositoryRestResource(collectionResourceRel = "inlinenestedentity", path = "inlinenestedentity")
public interface InlineNestedEntityRepository extends PagingAndSortingRepository<InlineNestedEntity, Long>, JpaSpecificationExecutor<InlineNestedEntity> {

    List<InlineNestedEntity> findAllByParentEntityId(String parentEntityId);
}


