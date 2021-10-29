
package ml.kalanblowSystemManagement.model;

import java.time.LocalDate;
import java.time.Year;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(
        chain = true)

@JsonInclude(
        content = Include.NON_NULL)

@JsonIgnoreProperties(
        ignoreUnknown = true)
@NoArgsConstructor

@PrimaryKeyJoinColumn(
        name = "user_id")

@DiscriminatorColumn(
        name = "STUDENT")

@Entity

@Table(
        name = "student")

@AllArgsConstructor 
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student extends User {

    private static final long serialVersionUID = 1L;

    @Column(
            name = "age")
    @PositiveOrZero(
            message = "You cannot have negative numbers of age.")
    private int age;

    @Column(
            name = "studentNumeroIne")
    private String studentNumeroIne;

    @Column(
            name = "entry_date",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @DateTimeFormat(
            pattern = "dd/MM/yyyy")
    private LocalDate entryDate;

    @Column(
            name = "school_year")
    @Pattern(
            regexp = "^[12][0-9]{3}$",
            message = "le format de l'année scolaire est de 4 chiffres, ex 2021")
    @NotNull(
            message = "le format de l'année scolaire est de 4 chiffres, ex 2021")
    private Integer schoolYear = Year.now().getValue();

}
