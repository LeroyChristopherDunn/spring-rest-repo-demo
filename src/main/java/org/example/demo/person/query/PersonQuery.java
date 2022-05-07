package org.example.demo.person.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonQuery {
    private Integer[] ids;
    private String firstName;
    private String firstNameGreaterThan;
    private String lastName;
    private String lastNameIgnoreCase;
    private String lastNameLike;
}
