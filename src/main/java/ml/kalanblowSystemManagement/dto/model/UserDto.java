package ml.kalanblowSystemManagement.dto.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Setter
@Getter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

	private String email;

	private String firstName;

	private String lastName;

	private String password;

	private boolean isAdmin;

	private String mobileNumber;
	
	private Set<RoleDto> roleDtos = new HashSet<>();
	public String getFullName() {
        return firstName != null ? firstName.concat(" ").concat(lastName) : "";
    }

}
