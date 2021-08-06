package ml.kalanblowSystemManagement.dto.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class UserDto {

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

	@ApiModelProperty
	private Set<RoleDto> roleDtos = new HashSet<>();

	public String getFullName() {
		return firstName != null ? firstName.concat(" ").concat(lastName) : "";
	}

}
