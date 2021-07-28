package ml.kalanblowSystemManagement.controller.web.command;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class AdminSignupCommand {

	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	@NotNull
	private String password;
	@NotNull
	private String email;
	
	

}
