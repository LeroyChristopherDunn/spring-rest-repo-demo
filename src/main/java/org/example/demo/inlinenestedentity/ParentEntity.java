package org.example.demo.inlinenestedentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentEntity {

    @NotBlank @Id
    private String id;

    @NotBlank
    private String title;

    @OneToMany(mappedBy = "parentEntityId", cascade = CascadeType.ALL, orphanRemoval = true)
    // exported false makes entity inline
    @RestResource(path = "inlinenestedentities", rel="inlinenestedentities", exported = false)
    private List<InlineNestedEntity> inlineNestedEntities;

    // spring errors if list pointer is reassigned
    public void setInlineNestedEntities(List<InlineNestedEntity> inlineNestedEntities) {
        if (this.inlineNestedEntities != null){
            this.inlineNestedEntities.clear();
            this.inlineNestedEntities.addAll(inlineNestedEntities);
            return;
        }
        this.inlineNestedEntities = inlineNestedEntities;
    }
}
