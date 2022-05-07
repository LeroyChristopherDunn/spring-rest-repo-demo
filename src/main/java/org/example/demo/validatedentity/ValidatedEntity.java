package org.example.demo.validatedentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidatedEntity {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String notBlankString;

    @Pattern(regexp=".*\\S.*", message = "must not be whitespace")
    private String notWhitespaceString;

    @NotNull
    @Column(nullable = false)
    private String notNull;

    private Boolean bool;

    private Date date;

    @PositiveOrZero
    private Integer positiveInt;
}
