
package ml.kalanblowSystemManagement.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter
@Getter
public class Addresse {

    @Column(
            insertable = true,
            updatable = true)
    private String street;

    @Column(
            insertable = true,
            updatable = true)
    private int streetNumber;

    @Column(
            insertable = true,
            updatable = true)
    private String city;

    @Column(
            insertable = true,
            updatable = true)
    private String state;

    @Column(
            insertable = true,
            updatable = true)
    private Integer codePostale;

    @Column(
            insertable = true,
            updatable = true)
    private String country;
}
