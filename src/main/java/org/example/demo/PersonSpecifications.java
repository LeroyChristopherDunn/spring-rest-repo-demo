package org.example.demo;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://stackoverflow.com/a/69909639/5771199

public class PersonSpecifications {
    public static Specification<Person> getSpecification(PersonQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getIds() != null) {
                predicates.add(root.get("id").in(Arrays.asList(query.getIds())));
            }

            if (query.getFirstName() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("firstName"),
                        query.getFirstName()
                ));
            }

            if (query.getFirstNameGreaterThan() != null) {
                predicates.add(criteriaBuilder.greaterThan(
                        root.get("firstName"),
                        query.getFirstNameGreaterThan()
                ));
            }

            if (query.getLastName() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("lastName"),
                        query.getLastName()
                ));
            }

            if (query.getLastNameIgnoreCase() != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("lastName")),
                        query.getLastNameIgnoreCase().toLowerCase()
                ));
            }

            if (query.getLastNameLike() != null) {
                predicates.add(criteriaBuilder.like(
                        root.get("lastName"),
                        "%" + query.getLastNameLike() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
