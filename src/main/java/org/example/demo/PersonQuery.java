package org.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonQuery {
    private String firstName;
    private String lastName;
}
