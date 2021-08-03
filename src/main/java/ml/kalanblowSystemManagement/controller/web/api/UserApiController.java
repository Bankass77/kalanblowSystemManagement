package ml.kalanblowSystemManagement.controller.web.api;

import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.request.UserSignupRequest;
import ml.kalanblowSystemManagement.dto.mapper.UserMapper;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.dto.response.Response;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.service.UserService;

@RestController
@RequestMapping(value="/api/v1/user",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(value = "KSM-application", description = "Operations pertaining to user management in the KSM application")
public class UserApiController {

	private UserService userService;

	@Autowired
	public UserApiController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping(value = "/signup")
	public Response creatnewUserAdmin() {
		return Response.ok();

	}

	/**
	 * @param pageable
	 * @return
	 */
	@GetMapping(value = "/queryByPage")
	 @ApiOperation(value = "Get All User Instances Reference IDs")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully Get All User Reference IDs"),
	      @ApiResponse(code = 404, message = "No User Reference IDs Found"),
	      @ApiResponse(code = 500, message = "Internal Server Error")
	  })
	public Page<UserDto> querybyPage(Pageable pageable) {

		Page<UserDto> pageInfo = userService.listUserByPage(pageable);
		return pageInfo;
	}

	/**
	 * @param userSignupRequest
	 * @return
	 */
	@PostMapping(value = "/signup")
	@ApiOperation(value = "${AdminController.signup}")
	 @ApiResponses(value = {
		      @ApiResponse(code = 200, message = "Successfully User Created"),
		      @ApiResponse(code = 500, message = "Internal Server Error")
		 })
	public Response signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {

		return Response.ok().setPayload(registerUser(userSignupRequest, false));

	}

	private UserDto registerUser(@Valid UserSignupRequest userSignupRequest, boolean isAdmin) {

		UserDto userDto = new UserDto().setEmail(userSignupRequest.getEmail())
				.setFirstName(userSignupRequest.getFirstName()).setLastName(userSignupRequest.getLastNme())
				.setPassword(userSignupRequest.getPassword()).setAdmin(isAdmin);

		return userService.signup(userDto);
	}

	/**
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Get a Specific User Instance by ID")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully Get User By ID"),
	      @ApiResponse(code = 404, message = "No User Found With ID"),
	      @ApiResponse(code = 500, message = "Internal Server Error")
	  })
	public Response getUserById(@PathVariable("id") int id) {

		Optional<UserDto> userDto = Optional.ofNullable(userService.findUserById((long) id));
		log.debug("User is:{}", userDto.get());
		return Response.ok().setPayload(userDto.get());
	}

	/**
	 * @param userDto
	 * @param id
	 * @return
	 */
	@PutMapping(value = "/{id}")
	@ApiOperation(value = "Put a Specific User Instance by ID")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully Put the User By ID"),
	      @ApiResponse(code = 400, message = "Bad Request for Putting the User"),
	      @ApiResponse(code = 404, message = "No User Found With ID"),
	      @ApiResponse(code = 500, message = "Internal Server Error")
	  })
	public Response updateUserById(@RequestBody Optional<UserDto> userDto, @PathVariable("id") int id) {

		userDto = Optional.ofNullable(userService.findUserById((long) id));
		User user = new ModelMapper().map(userDto, User.class);
		user.setId((long) id);

		UserDto updateUser = UserMapper.userToUserDto(user);

		userService.updateUserProfile(updateUser);
		return Response.ok().setPayload(updateUser);

	}

	/**
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@ApiOperation(value = "Delete a Specific User Instance by ID")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully Put the User By ID"),
	      @ApiResponse(code = 400, message = "Bad Request for Putting the User"),
	      @ApiResponse(code = 404, message = "No User Found With ID"),
	      @ApiResponse(code = 500, message = "Internal Server Error")
	  })
	public Response delete(@PathVariable int id) {

		UserDto userDto = userService.deleteUserById((long) id);

		return Response.ok().setPayload(userDto);
	}

	@PostMapping("/profile/email")
	public Response saveUserEmail(@RequestBody String email) {
		UserDto userDto = userService.changeUserEmail(email);
		return Response.ok().setPayload(userDto);
	}

	@PostMapping("/profile/password")
	public Response saveUserPassword(

			@RequestPart("oldPassword") String oldPassword,

			@RequestPart("newPassword") String newPassword) {
		UserDto userDto = userService.changeUserPassword(oldPassword, newPassword);
		return Response.ok().setPayload(userDto);
	}

	/*
	 * @GetMapping("/profile/userDetails") public Response getUserDetails() {
	 * Authentication authentication =
	 * SecurityContextHolder.getContext().getAuthentication(); return
	 * userService.findUserByEmail(authentication.getName()).map(Response.ok().
	 * getStatus()) .orElse(Response.notFound().getErrors()); }
	 */
	@PutMapping("/profile/userDetails")
	@ApiOperation(value = "Put a Specific User Instance by ID")
	  @ApiResponses(value = {
	      @ApiResponse(code = 200, message = "Successfully Put the User By ID"),
	      @ApiResponse(code = 400, message = "Bad Request for Putting the User"),
	      @ApiResponse(code = 404, message = "No User Found With ID"),
	      @ApiResponse(code = 500, message = "Internal Server Error")
	  })
	public Response updateUserDetails(@Valid @RequestBody UserDto user, BindingResult result) {
		if (result.hasErrors()) {
			return Response.ok();
		}
		UserDto updatedUser = userService.updateUserProfile(user);
		return Response.ok();
	}
}