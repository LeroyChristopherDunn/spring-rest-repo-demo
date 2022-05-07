package org.example.demo.dynamicquery.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DynamicQueryEntityQuery {
    private Integer[] ids;
    private String firstName;
    private String firstNameGreaterThan;
    private String lastName;
    private String lastNameIgnoreCase;
    private String lastNameLike;
}
