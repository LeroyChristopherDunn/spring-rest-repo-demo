package org.example.demo.inlinenestedentity;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@Tag(name = "InlineNestedEntities")
@RepositoryRestResource(collectionResourceRel = "parententity", path = "parententity")
public interface ParentEntityRepository extends PagingAndSortingRepository<ParentEntity, String>, JpaSpecificationExecutor<ParentEntity> {

}


