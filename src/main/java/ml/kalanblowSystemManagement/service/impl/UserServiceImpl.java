package ml.kalanblowSystemManagement.service.impl;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.maxmind.geoip2.DatabaseReader;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.config.PropertiesConfig;
import ml.kalanblowSystemManagement.dto.mapper.UserMapper;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.exception.EntityType;
import ml.kalanblowSystemManagement.exception.ExceptionType;
import ml.kalanblowSystemManagement.exception.KalanblowSystemManagementException;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserLocation;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.model.UserSpecification;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.repository.UserLocationRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;
import ml.kalanblowSystemManagement.service.UserService;

@Component(value = "userService")
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final ModelMapper modelMapper;

	private final BCryptPasswordEncoder passwordEncoder;

	@Qualifier("GeoIPCountry")
	private final DatabaseReader databaseReader;

	private final UserLocationRepository userLocationRepository;

	private PropertiesConfig propertiesConfig;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper,
			BCryptPasswordEncoder passwordEncoder, DatabaseReader databaseReader,
			UserLocationRepository userLocationRepository, PropertiesConfig propertiesConfig) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.databaseReader = databaseReader;
		this.userLocationRepository = userLocationRepository;
		this.propertiesConfig = propertiesConfig;
	}

	@Override
	@SneakyThrows
	public UserDto findUserById(Long id) {

		User user = userRepository.findUserById(id)
				.orElseThrow(() -> exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, "Invalid user Id:" + id));

		return UserMapper.userToUserDto(user);
	}

	@Override
	public Optional<UserDto> findUserByEmail(String email) {

		Optional<User> user = userRepository.findByEmail(email);

		if (user.isPresent()) {
			log.debug("findUserByEmail:{}", user.get());
			return Optional.ofNullable(UserMapper.userToUserDto(user.get()));
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

		if (!emailExist(userDto.getEmail())) {

			throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY,
					"An user with that email adress already exists:" + userDto.getEmail());
		}
		Role userRole = new Role();

		if (userDto.isAdmin()) {
			userRole = roleRepository.findRoleByUserRoleName(UserRole.ADMIN);
		} else {
			userRole = roleRepository.findRoleByUserRoleName(UserRole.STUDENT);
		}
		User user = new User().setBirthDate(userDto.getBirthDate()).setEmail(userDto.getEmail())
				.setFirstName(userDto.getFirstName()).setLastName(userDto.getLastName())
				.setMatchingPassword(userDto.getMatchingPassword()).setPassword(userDto.getPassword())
				.setMobileNumber(userDto.getMobileNumber()).setRoles(new HashSet<>(Arrays.asList(userRole)));

		return UserMapper.userToUserDto(userRepository.save(user));
	}

	@Override
	public UserDto changeUserPassword(String oldPassword, String newPassword) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String actualEmail = authentication.getName();
		Optional<User> uOptional = userRepository.findByEmail(actualEmail);
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
				unmodifiableMsg + " " + "with this " + actualEmail);

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

	/**
	 * @param lastName
	 * @return
	 */
	@Override
	public Set<UserDto> findUserByLastName(String lastName) {

		List<User> users = userRepository.findAllByLastNameContainingIgnoreCaseOrderByIdAsc(lastName);
		List<UserDto> userDtos = users.stream().map(UserMapper::userToUserDto).collect(Collectors.toList());
		return new HashSet<>(userDtos);
	}

	/**
	 * @param newEmail
	 * @return
	 */
	@Override
	public UserDto changeUserEmail(String newEmail) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String actualEmail = authentication.getName();

		String unmodifiableMsg = "You cannot change this user's email.";

		if (actualEmail.equals(newEmail)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"The user with the given e-mail already exists. Enter a different e-mail");
		}
		Optional<User> userByEmail = userRepository.findByEmail(newEmail);
		userByEmail.ifPresent(u -> {
			throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, userByEmail.get().getEmail());
		});
		Optional<User> userOpt = userRepository.findByEmail(actualEmail);
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

		Optional<User> oldUser = userRepository.findByEmail(userDto.getEmail());
		if (oldUser.isPresent()) {

			UserMapper.userToUserDto(userRepository.save(oldUser.get()));
		} else {
			throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND,
					"Invalid user email:" + "" + userDto.getEmail());
		}

	}

	@Override
	public Set<UserDto> findUserbyMobileNumber(String mobileNumber) {
		List<User> users = userRepository.findUserByMobileNumber(mobileNumber);
		List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return new HashSet<>(userDtos);
	}

	@Override
	public boolean emailExist(String email) {

		return userRepository.findByEmail(email) != null;
	}

	@Override
	public UserDto updateUserProfile(UserDto userDto) {

		return null;
	}

	@Override
	public Page<UserDto> findAllPageableOrderByLastName(String queryString, Pageable pageable) {
		UserSpecification userSpecification = new UserSpecification(queryString);

		Page<User> uPage = userRepository.findAll(userSpecification, pageable);

		int totalElements = (int) uPage.getTotalElements();

		return new PageImpl<>(uPage.stream().map(userDto -> new UserDto()).collect(Collectors.toList()), pageable,
				totalElements);
	}

	@Override
	public void addUserLocation(User user, String ip) {

		if (!isGeoIpLibEnabled()) {
			return;
		}

		try {
			final InetAddress ipAddress = InetAddress.getByName(ip);
			final String country = databaseReader.country(ipAddress).getCountry().getName();
			UserLocation loc = new UserLocation(country, user);
			loc.setEnabled(true);
			userLocationRepository.save(loc);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isGeoIpLibEnabled() {
		return Boolean.parseBoolean(propertiesConfig.getConfigValue("geo.ip.lib.enabled").toString());
	}

	@Override
	public Page<UserDto> findAllPageable(PageRequest of) {
		int pageSize = of.getPageSize();
		int currentPage = of.getPageNumber();
		int startItem = currentPage * pageSize;

		List<User> user = userRepository.findAll();
		List<User> list;

		if (user.size() < startItem) {

			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, user.size());
			list = user.subList(startItem, toIndex);
		}
		Page<User> users = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize), user.size());
		List<UserDto> userDtos = users.stream().map(userDto -> modelMapper.map(userDto, UserDto.class))
				.collect(Collectors.toList());
		final Page<UserDto> uPage = new PageImpl<>(userDtos.subList(startItem, currentPage), of, userDtos.size());
		return uPage;
	}

}
