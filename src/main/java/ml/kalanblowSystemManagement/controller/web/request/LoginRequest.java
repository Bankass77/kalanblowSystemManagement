package ml.kalanblowSystemManagement.controller.web.request;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
	  private String password;
	  private Boolean rememberMe;

}
