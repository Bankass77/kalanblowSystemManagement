
package ml.kalanblowSystemManagement.controller.web.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ml.kalanblowSystemManagement.annotation.EmailContraint;
import ml.kalanblowSystemManagement.annotation.FrenchPhoneConstraint;
import ml.kalanblowSystemManagement.annotation.ValidPassword;
import ml.kalanblowSystemManagement.exception.NotExistingUser;
import ml.kalanblowSystemManagement.model.Addresse;
import ml.kalanblowSystemManagement.model.Gender;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.ValidationGroupOne;
import ml.kalanblowSystemManagement.model.ValidationGroupTwo;
import ml.kalanblowSystemManagement.utils.GenderConverter;

@Accessors(
        chain = true)
@Getter
@Setter
@NotExistingUser(
        groups = ValidationGroupTwo.class)
@ValidPassword(
        groups = ValidationGroupOne.class)
public class AdminSignupCommand {

    @Convert(
            converter = GenderConverter.class)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Size(
            min = 1,
            max = 200,
            groups = ValidationGroupOne.class)
    private String firstName;

    @NotNull
    @Size(
            min = 1,
            max = 200,
            groups = ValidationGroupOne.class)
    private String lastName;
    @NotNull
    private String password;

    @NotNull
    private String matchingPassword;

    @NotNull
    @EmailContraint(
            groups = ValidationGroupOne.class)
    private String email;

    @NotNull
    @FrenchPhoneConstraint(
            groups = ValidationGroupOne.class)
    private String mobileNumber;

    @NotNull
    private LocalDate birthDate;

    private Set<Role> roles = new HashSet<Role>();

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    Long createdBy;
    
    private Addresse adresse;

}
