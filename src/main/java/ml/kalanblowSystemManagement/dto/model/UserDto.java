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
import ml.kalanblowSystemManagement.constraint.EmailContraint;
import ml.kalanblowSystemManagement.constraint.PasswordConstraint;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Setter
@Getter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

	@EmailContraint
	@NotNull
	@NotEmpty
	@ApiModelProperty(notes = "user email")
	private String email;

	@NotNull
	@NotEmpty
	@ApiModelProperty
	private String firstName;

	@NotNull
	@NotEmpty
	@ApiModelProperty
	private String lastName;

	@NotNull
	@NotEmpty
	@PasswordConstraint
	private String password;

	@ApiModelProperty
	private boolean isAdmin;

	@ApiModelProperty
	private String mobileNumber;

	@NotNull
	@ApiModelProperty
	private LocalDate birthDate;

	@NotNull
	@NotEmpty
	@PasswordConstraint
	private String matchingPassword;

	@ApiModelProperty
	private Set<RoleDto> roleDtos = new HashSet<>();

	public String getFullName() {
		return firstName != null ? firstName.concat(" ").concat(lastName) : "";
	}

}
