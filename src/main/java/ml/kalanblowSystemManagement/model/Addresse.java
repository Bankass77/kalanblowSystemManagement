
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
            insertable = false,
            updatable = false)
    private String street;

    @Column(
            insertable = false,
            updatable = false)
    private int streetNumber;

    @Column(
            insertable = false,
            updatable = false)
    private String city;

    @Column(
            insertable = false,
            updatable = false)
    private String state;

    @Column(
            insertable = false,
            updatable = false)
    private Integer codePostale;

    @Column(
            insertable = false,
            updatable = false)
    private String country;
}
