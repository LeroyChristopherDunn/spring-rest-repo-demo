package org.example.demo.dynamicquery;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@Tag(name = "DynamicQuery")
@RepositoryRestResource(collectionResourceRel = "dynamicquery", path = "dynamicquery")
public interface DynamicQueryRepository extends PagingAndSortingRepository<DynamicQueryEntity, Long>, JpaSpecificationExecutor<DynamicQueryEntity> {

}
