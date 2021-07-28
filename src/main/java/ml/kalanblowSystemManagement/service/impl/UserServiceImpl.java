package ml.kalanblowSystemManagement.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ml.kalanblowSystemManagement.dto.mapper.UserMapper;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
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

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Optional<UserDto> findUserById(Long id) {

		return Optional.ofNullable(findUserById(id).get());
	}

	@Override
	public Optional<UserDto> findUserByEmail(String email) {

		return Optional.ofNullable(modelMapper.map(userRepository.findUserByEmail(email), UserDto.class));
	}

	@Override
	public Optional<UserDto> findUserByfirstNameAndLastName(String firstName, String lastName) {

		return Optional.ofNullable(
				modelMapper.map(userRepository.findUserByfirstNameAndLastName(firstName, lastName), UserDto.class));
	}

	@Override
	public Optional<UserDto> findUserByRoles(UserRole name) {

		return Optional.ofNullable(modelMapper.map(userRepository.findUserByRoles(name), UserDto.class));
	}

	@Override
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
					.setLastName(userDto.getLastName()).setPassword(userDto.getPassword())
					.setRoles(new HashSet<>(Arrays.asList(userRole)));
			return UserMapper.userToUserDto(userRepository.save(user));
		}
		return userDto;

		
	}

	@Override
	public UserDto updateUserProfile(UserDto userDto) {

		Optional<User> uOptional = Optional.ofNullable(userRepository.findUserByEmail(userDto.getEmail()));

		if (uOptional.isPresent()) {

			User updateUser = uOptional.get().setEmail(userDto.getEmail()).setFirstName(userDto.getFirstName())
					.setLastName(userDto.getLastName()).setRoles(uOptional.get().getRoles());
			return UserMapper.userToUserDto(userRepository.save(updateUser));
		}
		return userDto;

	}

	@Override
	public UserDto changeUserPassword(UserDto userDto, String newPassword) {

		userDto.setPassword(newPassword);
		userRepository.save(modelMapper.map(userDto, User.class));

		return userDto;
	}

	@Override
	public UserDto deleteUserById(Long id) {

		return Optional.ofNullable(modelMapper.map(userRepository.deleteUserById(id), UserDto.class)).get();
	}

	@Override
	public UserDto deleteUserByEmail(String email) {

		return Optional.ofNullable(modelMapper.map(userRepository.deleteUserByEmail(email), UserDto.class)).get();
	}

	@Override
	public Set<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return new HashSet<>(userDtos);
	}

}
