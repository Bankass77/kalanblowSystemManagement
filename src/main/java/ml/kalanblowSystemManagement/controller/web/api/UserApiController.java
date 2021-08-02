package ml.kalanblowSystemManagement.controller.web.api;

import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.controller.web.request.UserSignupRequest;
import ml.kalanblowSystemManagement.dto.mapper.UserMapper;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.dto.response.Response;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
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
	public Response getUserById(@PathVariable("id") int id) {

		Optional<UserDto> userDto = Optional.ofNullable(userService.findUserById((long) id));
		log.debug("User is:{}",userDto.get());
		return Response.ok().setPayload(userDto.get());
	}

	/**
	 * @param userDto
	 * @param id
	 * @return
	 */
	@PutMapping(value = "/{id}")
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
	public Response delete(@PathVariable int id) {

		UserDto userDto = userService.deleteUserById((long) id);

		return Response.ok().setPayload(userDto);
	}
}