package org.example.demo;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

// https://stackoverflow.com/a/69909639/5771199

public class PersonSpecifications {
    public static Specification<Person> getSpecification(PersonQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getFirstName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("firstName"), query.getFirstName()));
            }

            if (query.getLastName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("lastName"), query.getLastName()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
