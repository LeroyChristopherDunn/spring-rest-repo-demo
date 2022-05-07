package org.example.demo.inlinenestedentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InlineNestedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable = false)
    private String name;

    @NotNull @Column(name = "parent_entity_id", nullable = false)
    private String parentEntityId;

    public InlineNestedEntity(@NotBlank String name, @NotBlank String parentEntityId) {
        this.name = name;
        this.parentEntityId = parentEntityId;
    }
}
