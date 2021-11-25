
package ml.kalanblowsystemmanagement.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
        name = "TEACHER")

@Entity

@Table(
        name = "teacher")
@Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE)
public class Teacher extends User {

    private static final long serialVersionUID = 1L;

    @Column
    private String degree;

    @Column
    private String matricule;

    @Column(
            name = "entry_date",
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")

    @DateTimeFormat(
            pattern = "dd/MM/yyyy")
    private LocalDate entryDate;

}
