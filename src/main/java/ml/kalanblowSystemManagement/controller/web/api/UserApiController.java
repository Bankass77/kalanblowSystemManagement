package ml.kalanblowSystemManagement.controller.web.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ml.kalanblowSystemManagement.controller.web.request.UserSignupRequest;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.dto.response.Response;
import ml.kalanblowSystemManagement.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@Api(value = "brs-application", description = "Operations pertaining to user management in the BRS application")
public class UserApiController {

	private UserService userService;

	@Autowired
	public UserApiController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping(value = "/signup")
	@ApiOperation(value = "${AdminController.signup}")
	public Response signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {

		return Response.ok().setPayload(registerUser(userSignupRequest, false));

	}

	private UserDto registerUser(@Valid UserSignupRequest userSignupRequest, boolean isAdmin) {

		UserDto userDto = new UserDto().setEmail(userSignupRequest.getEmail())
				.setFirstName(userSignupRequest.getFirstName()).setLastName(userSignupRequest.getLastNme())
				.setPassword(userSignupRequest.getPassword()).setAdmin(isAdmin);

		return userService.signup(userDto);
	}

}
