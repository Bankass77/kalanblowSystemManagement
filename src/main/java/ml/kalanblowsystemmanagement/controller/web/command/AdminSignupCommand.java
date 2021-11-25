
package ml.kalanblowsystemmanagement.controller.web.command;

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
import ml.kalanblowsystemmanagement.annotation.EmailContraint;
import ml.kalanblowsystemmanagement.annotation.FrenchPhoneConstraint;
import ml.kalanblowsystemmanagement.annotation.ValidPassword;
import ml.kalanblowsystemmanagement.exception.NotExistingUser;
import ml.kalanblowsystemmanagement.model.Addresse;
import ml.kalanblowsystemmanagement.model.Gender;
import ml.kalanblowsystemmanagement.model.Role;
import ml.kalanblowsystemmanagement.model.ValidationGroupOne;
import ml.kalanblowsystemmanagement.model.ValidationGroupTwo;
import ml.kalanblowsystemmanagement.utils.GenderConverter;

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
