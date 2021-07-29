package ml.kalanblowSystemManagement.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@RestController
@RequestMapping("/api")
@Api(value = "KSM-application", description = "Operations pertaining to user login and logout in the KSM application")
public class FakeController {
	@ApiOperation("Login")
	@PostMapping("/auth")
	public void fakeLogin(@RequestBody @Valid LoginRequest loginRequest) {
		throw new IllegalStateException(
				"This method shouldn't be called. It's implemented by Spring Security filters.");
	}

	@ApiOperation("Logout")
	@PostMapping("/logout")
	public void fakeLogout() {
		throw new IllegalStateException(
				"This method shouldn't be called. It's implemented by Spring Security filters.");
	}

	@Getter
	@Setter
	@Accessors(chain = true)
	@NoArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class LoginRequest {
		@NotNull(message = "{constraints.NotEmpty.message}")
		private String email;
		@NotNull(message = "{constraints.NotEmpty.message}")
		private String password;
	}
}
