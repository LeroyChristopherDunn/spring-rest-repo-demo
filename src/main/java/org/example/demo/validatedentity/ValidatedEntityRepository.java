package org.example.demo.validatedentity;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@Tag(name = "Validated")
@RepositoryRestResource(collectionResourceRel = "validated", path = "validated")
public interface ValidatedEntityRepository extends PagingAndSortingRepository<ValidatedEntity, Long>, JpaSpecificationExecutor<ValidatedEntity> {

}
