package ml.kalanblowsystemmanagement.controller.web.command;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProfileFormCommand {

	@NotBlank
	@Size(min = 1, max = 40)
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	@Size(min = 5, max = 13)
	private String mobileNumber;

}
