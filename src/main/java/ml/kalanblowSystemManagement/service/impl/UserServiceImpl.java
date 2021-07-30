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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.SneakyThrows;
import ml.kalanblowSystemManagement.dto.mapper.UserMapper;
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
	public Optional<UserDto> findUserById(Long id) {

		return Optional.ofNullable(findUserById(id).get());
	}

	@Override
	public Optional<UserDto> findUserByEmail(String email) {

		if (email != null) {

			return Optional.ofNullable(modelMapper.map(userRepository.findUserByEmail(email), UserDto.class));
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email);
	}

	@Override
	@SneakyThrows
	public Optional<UserDto> findUserByfirstNameAndLastName(String firstName, String lastName) {

		if (firstName != null && lastName != null) {
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
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, name.name());
	}

	@Override
	@SneakyThrows
	public UserDto signup(UserDto userDto) {

		User user = userRepository.findUserByEmail(userDto.getEmail());

		Role userRole = new Role();
		if (user == null) {

			if (userDto.isAdmin()) {

				userRole = roleRepository.findRoleByUserRoleName(UserRole.ADMIN);
			} else {
				userRole = roleRepository.findRoleByUserRoleName(UserRole.STUDENT);
			}

			user = new User().setEmail(userDto.getEmail()).setFirstName(userDto.getFirstName())
					.setLastName(userDto.getLastName()).setPassword(passwordEncoder.encode(userDto.getPassword()))
					.setRoles(new HashSet<>(Arrays.asList(userRole)));
			return UserMapper.userToUserDto(userRepository.save(user));
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userDto.getEmail());
	}

	@Override
	@SneakyThrows
	public UserDto updateUserProfile(UserDto userDto) {

		Optional<User> uOptional = Optional.ofNullable(userRepository.findUserByEmail(userDto.getEmail()));

		if (uOptional.isPresent()) {

			User updateUser = uOptional.get().setEmail(userDto.getEmail()).setFirstName(userDto.getFirstName())
					.setLastName(userDto.getLastName()).setRoles(uOptional.get().getRoles())
					.setPassword(passwordEncoder.encode(userDto.getPassword()));
			return UserMapper.userToUserDto(userRepository.save(updateUser));
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userDto.getEmail());

	}

	@Override
	public UserDto changeUserPassword(UserDto userDto, String newPassword) {
		Optional<User> uOptional = Optional.ofNullable(userRepository.findUserByEmail(userDto.getEmail()));
		if (uOptional.isPresent()) {

			User user = uOptional.get();
			user.setPassword(passwordEncoder.encode(newPassword));
			return UserMapper.userToUserDto(userRepository.save(user));

		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, userDto.getEmail());

	}

	@Override
	@SneakyThrows
	public UserDto deleteUserById(Long id) {

		if (id != null) {
			return Optional.ofNullable(modelMapper.map(userRepository.deleteUserById(id), UserDto.class)).get();
		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, String.valueOf(id));
	}

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

	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return KalanblowSystemManagementException.throwException(entityType, exceptionType, args);

	}

}
