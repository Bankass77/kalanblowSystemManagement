package ml.kalanblowSystemManagement.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.dto.mapper.UserMapper;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.exception.EntityType;
import ml.kalanblowSystemManagement.exception.ExceptionType;
import ml.kalanblowSystemManagement.exception.KalanblowSystemManagementException;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;
import ml.kalanblowSystemManagement.service.UserService;

@Service
@Transactional
@Slf4j

public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final ModelMapper modelMapper;

	private final BCryptPasswordEncoder passwordEncoder;


	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = bCryptPasswordEncoder;
	}

	@Override
	@SneakyThrows
	public UserDto findUserById(Long id) {

		User user = userRepository.findUserById(id)
				.orElseThrow(() -> exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, "Invalid user Id:" + id));

		return UserMapper.userToUserDto(user);
	}

	@Override
	public UserDto findUserByEmail(String email) {

		Optional<User> user = userRepository.findUserByEmail(email);

		if (user.isPresent()) {
			log.debug("findUserByEmail:{}", user.get());
			return UserMapper.userToUserDto(user.get());
		}

		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, "User with this: " + email + "Not found");

	}

	@Override
	@SneakyThrows
	public Optional<UserDto> findUserByfirstNameAndLastName(String firstName, String lastName) {

		if (firstName != null && lastName != null) {

			log.debug("findUserByfirstNameAndLastName:{}", Optional.ofNullable(modelMapper
					.map(userRepository.findUserByfirstNameAndLastName(firstName, lastName), UserDto.class)));
			return Optional.ofNullable(
					modelMapper.map(userRepository.findUserByfirstNameAndLastName(firstName, lastName), UserDto.class));

		}

		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND,
				firstName + firstName.concat("").concat(lastName));
	}

	@Override
	@SneakyThrows
	public Optional<UserDto> findUserByRoles(UserRole name) {

		if (name != null) {
			return Optional.ofNullable(modelMapper.map(userRepository.findUserByRoles(name), UserDto.class));
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, " " + name);
	}

	@Override
	@SneakyThrows
	public UserDto signup(UserDto userDto) {

		Optional<User> user = userRepository.findUserByEmail(userDto.getEmail());
		List<Role> roles = roleRepository.findAll();
		List<RoleDto> roleDtos = roles.stream().map(role -> modelMapper.map(role, RoleDto.class))
				.collect(Collectors.toList());
		Set<RoleDto> roleDtos2 = new HashSet<>(roleDtos);

		Role userRole = new Role();
		if (user.isPresent()) {

			if (userDto.isAdmin()) {

				userRole = roleRepository.findRoleByUserRoleName(UserRole.ADMIN);
				roleDtos2.add(new ModelMapper().map(userRole, RoleDto.class));
			} else {
				userRole = roleRepository.findRoleByUserRoleName(UserRole.STUDENT);
				roleDtos2.add(new ModelMapper().map(userRole, RoleDto.class));
			}

			user = Optional.ofNullable(new User().setEmail(userDto.getEmail()).setFirstName(userDto.getFirstName())
					.setLastName(userDto.getLastName()).setPassword(passwordEncoder.encode(userDto.getPassword()))
					.setRoles(new HashSet<>(Arrays.asList(userRole))).setMobileNumber(userDto.getMobileNumber()));
			log.debug("signup new user:{}", UserMapper.userToUserDto(userRepository.save(user.get())));
			return UserMapper.userToUserDto(userRepository.save(user.get()));
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userDto.getEmail());
	}

	@Override
	@SneakyThrows
	public UserDto updateUserProfile(UserDto userDto) {

		Optional<User> uOptional = userRepository.findUserByEmail(userDto.getEmail());

		uOptional.ifPresent(user -> {

			if (!user.getEmail().equals(userDto.getEmail())) {
				throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, "The user is not found");
			}
		});

		return signup(userDto);
	}

	/**
	 *
	 */
	@Override
	public UserDto changeUserPassword(UserDto userDto, String newPassword) {
		Optional<User> uOptional = userRepository.findUserByEmail(userDto.getEmail());
		String unmodifiableMsg = "You cannot change this user's password.";
		if (uOptional.isPresent()) {

			UserDto uDto = UserMapper.userToUserDto(uOptional.get());
			if (!passwordEncoder.matches(uDto.getPassword(), newPassword)) {

				throw exception(EntityType.USER, ExceptionType.ENTITY_EXCEPTION,
						"he old password is incorrect. Try again");
			}
			if (uDto.getPassword().equals(newPassword)) {

				throw exception(EntityType.USER, ExceptionType.ENTITY_EXCEPTION,
						"The old password and the new one are the same. Enter a different password");
			}

			String newPasswordHashed = passwordEncoder.encode(newPassword);
			uDto.setPassword(newPasswordHashed);

			return signup(uDto);

		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND,
				unmodifiableMsg + " " + "with this " + userDto.getEmail());

	}

	@Override
	@SneakyThrows
	public UserDto deleteUserById(Long id) {

		if (id != null) {
			return Optional.ofNullable(modelMapper.map(userRepository.deleteUserById(id), UserDto.class)).get();
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, String.valueOf(id));
	}

	/**
	 *
	 */
	@Override
	@SneakyThrows
	public UserDto deleteUserByEmail(String email) {

		if (email != null) {
			return Optional.ofNullable(modelMapper.map(userRepository.deleteUserByEmail(email), UserDto.class)).get();
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
	}

	@Override
	public Set<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();

		if (!users.isEmpty()) {
			List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class))
					.collect(Collectors.toList());
			return new HashSet<>(userDtos);
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, String.valueOf(users.size()));

	}

	/**
	 *
	 */
	@Override
	public Page<UserDto> listUserByPage(Pageable pageable) {

		List<User> users = userRepository.findAll();
		if (!users.isEmpty()) {
			List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class))
					.collect(Collectors.toList());
			final int start = (int) pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), userDtos.size());
			final Page<UserDto> uPage = new PageImpl<>(userDtos.subList(start, end), pageable, userDtos.size());
			return uPage;
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, String.valueOf(users.size()));

	}

	List<UserDto> findByLastName(String lastName) {
		return userRepository.findAllByLastNameContainingIgnoreCaseOrderByIdAsc(lastName).stream()
				.map(UserMapper::userToUserDto).collect(Collectors.toList());
	}

	/**
	 * @param newEmail
	 * @return
	 */
	UserDto changeUserEmail(String newEmail) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String actualEmail = authentication.getName();

		String unmodifiableMsg = "You cannot change this user's email.";

		if (actualEmail.equals(newEmail)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"The user with the given e-mail already exists. Enter a different e-mail");
		}
		Optional<User> userByEmail = userRepository.findUserByEmail(newEmail);
		userByEmail.ifPresent(u -> {
			throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, userByEmail.get().getEmail());
		});
		Optional<User> userOpt = userRepository.findUserByEmail(actualEmail);
		if (userOpt.isPresent()) {
			UserDto userDto = UserMapper.userToUserDto(userOpt.get());
			userDto.setEmail(newEmail);
			return signup(userDto);
		} else {
			throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND,
					unmodifiableMsg + " " + "The user is not found");
		}
	}

	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return KalanblowSystemManagementException.throwException(entityType, exceptionType, args);

	}

	@Override
	@SneakyThrows
	public void editUser(UserDto userDto) {

		Optional<User> oldUser = userRepository.findUserByEmail(userDto.getEmail());
		if (oldUser.isPresent()) {

			UserMapper.userToUserDto(userRepository.save(oldUser.get()));
		} else {
			throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND,
					"Invalid user email:" + "" + userDto.getEmail());
		}

	}

}
