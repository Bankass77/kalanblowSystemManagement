package ml.kalanblowSystemManagement.dto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ml.kalanblowSystemManagement.annotation.EmailContraint;
import ml.kalanblowSystemManagement.annotation.PasswordConstraintValueMatch;
import ml.kalanblowSystemManagement.annotation.ValidPassword;
import ml.kalanblowSystemManagement.model.Addresse;
import ml.kalanblowSystemManagement.model.Gender;


@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Setter
@Getter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

@PasswordConstraintValueMatch.List({
    @PasswordConstraintValueMatch(
            field = "password",
            fieldMatch = "matchingPassword",
            message = "Passwords do not match!"
    )
})
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UserDto {

	private Long id;
	
	@EmailContraint
	@NotNull
	@NotEmpty
	@ApiModelProperty(notes = "user email")
	private String email;

	@NotNull
	@NotEmpty
	@ApiModelProperty(example = "First Name")
	private String firstName;

	@NotNull
	@NotEmpty
	@ApiModelProperty(example = "Last Name")
	private String lastName;

	@NotNull
	@NotEmpty
	@ValidPassword
    @ApiModelProperty(example = "password")
	private String password;

	@ApiModelProperty(example = "isAdmin")
	private boolean isAdmin;

	@ApiModelProperty(example = "Mobile Number")
	private String mobileNumber;

	@NotNull
	@ApiModelProperty(example = "birth Date")
	private LocalDate birthDate;

	@NotNull
	@NotEmpty
	@ValidPassword
    @ApiModelProperty(example = "matchingPassword")
	private String matchingPassword;

	@ApiModelProperty(example = "authorithy")
	private Set<RoleDto> roles = new HashSet<>();
	
	@ApiModelProperty(example = "sexe")
	private Gender gender;
	
	@ApiModelProperty(example = "create date")
	private LocalDateTime createdDate;
	
	@ApiModelProperty(example = "last modify date")
	private LocalDateTime lastModifiedDate;
	
	@ApiModelProperty(example = "adresse")
	private Addresse adresse;
	
	@ApiModelProperty(example = "createdBy")
	private Long createdBy;
	
	public String getFullName() {
		return firstName != null ? firstName.concat(" ").concat(lastName) : "";
	}

}
