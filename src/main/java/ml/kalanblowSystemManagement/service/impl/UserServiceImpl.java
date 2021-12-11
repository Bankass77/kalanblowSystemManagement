
package ml.kalanblowSystemManagement.service.impl;

import static java.util.Collections.emptyList;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.exception.EntityType;
import ml.kalanblowSystemManagement.exception.ExceptionType;
import ml.kalanblowSystemManagement.exception.KalanblowSystemManagementException;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserLocation;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.model.UserSpecification;
import ml.kalanblowSystemManagement.repository.UserLocationRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;
import ml.kalanblowSystemManagement.service.RoleService;
import ml.kalanblowSystemManagement.service.UserService;

@Component(value = "userService")
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleService roleService;

	private final ModelMapper modelMapper;

	private final BCryptPasswordEncoder passwordEncoder;

	@Qualifier("GeoIPCountry")
	private final DatabaseReader databaseReader;

	private final UserLocationRepository userLocationRepository;

	private PropertiesConfig propertiesConfig;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper,
			BCryptPasswordEncoder passwordEncoder, DatabaseReader databaseReader,
			UserLocationRepository userLocationRepository, PropertiesConfig propertiesConfig,
			CacheManager cacheManager) {
		super();
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.databaseReader = databaseReader;
		this.userLocationRepository = userLocationRepository;
		this.propertiesConfig = propertiesConfig;
	}

	@Override
	@SneakyThrows
	@Cacheable(value = "cache.userById", key = "#id", unless = "#result == null", condition = "#id !=null")
	public UserDto findUserById(Long id) {

		User user = userRepository.findUserById(id)
				.orElseThrow(() -> exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, "Invalid user Id:" + id));

		return UserMapper.userToUserDto(user);
	}

	@Override
	@Cacheable(value = "cache.userByEmail", key = "#email", unless = "#result == null")
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
	@CacheEvict(value = { "cache.allUsersPageable", "cache.allUsers", "cache.userByEmail", "cache.userById",
			"cache.byNameContaining", "cache.byEmailContaining", "cache.changeUserEmail" }, allEntries = true)
	public UserDto signup(UserDto userDto) {

		if (!emailExist(userDto.getEmail())) {

			throw exception(EntityType.USER, ExceptionType.DUPLICATE_ENTITY,
					"An user with that email adress already exists:" + userDto.getEmail());
		}
		RoleDto userRole = new RoleDto();

		if (userDto.isAdmin()) {
			userRole = roleService.findByName(UserRole.ADMIN.getUserRole());
		} else {
			userRole = roleService.findByName(UserRole.STUDENT.getUserRole());
		}
		Role userRole2 = modelMapper.map(userRole, Role.class);

		User user = new User().setBirthDate(userDto.getBirthDate()).setEmail(userDto.getEmail())
				.setFirstName(userDto.getFirstName()).setLastName(userDto.getLastName())
				.setMatchingPassword(userDto.getMatchingPassword()).setPassword(userDto.getPassword())
				.setMobileNumber(userDto.getMobileNumber()).setAdresse(userDto.getAdresse())
				.setCreatedBy(userDto.getCreatedBy()).setCreatedDate(LocalDateTime.now())
				.setLastModifiedDate(LocalDateTime.now()).setPhoto(null)
				.setRoles(new HashSet<>(Arrays.asList(userRole2)));

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
	@CacheEvict(value = { "cache.allUsersPageable", "cache.allUsers", "cache.userByEmail", "cache.userById",
			"cache.allUsersEagerly", "cache.byNameContaining", "cache.byEmailContaining",
			"cache.changeUserEmail" }, allEntries = true)
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
	@Cacheable(value = "cache.allUsers")
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
	@Cacheable(value = "cache.allUsersPageable")
	public Page<UserDto> listUserByPage(Pageable pageable) {

		List<User> users = userRepository.findAll();
		Page<User> userPage = userRepository.findAll(pageable);

		if (!users.isEmpty()) {
			List<UserDto> userDtos = userPage.stream().map(user -> modelMapper.map(user, UserDto.class))
					.collect(Collectors.toList());

			final int start = (int) pageable.getOffset();

			final int end = Math.min((start + pageable.getPageSize()), users.size());

			return new PageImpl<UserDto>(userDtos.subList(start, end), pageable, userPage.getTotalElements());

		}
		throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, String.valueOf(userPage.getTotalElements()));

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
	@Cacheable(value = "cache.changeUserEmail", key = "#email", unless = "#result == null")
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
		Optional<User> oldUser = userRepository.findByEmail(userDto.getEmail());
		User persistedUser = new User().setAdresse(userDto.getAdresse()).setBirthDate(userDto.getBirthDate())
				.setEmail(userDto.getEmail()).setFirstName(userDto.getFirstName()).setGender(userDto.getGender())
				.setLastName(userDto.getLastName()).setMatchingPassword(userDto.getMatchingPassword())
				.setMobileNumber(userDto.getMobileNumber()).setPassword(userDto.getPassword())
				.setFirstName(userDto.getFullName()).setCreatedDate(userDto.getCreatedDate())
				.setCreatedBy(userDto.getCreatedBy()).setAdresse(userDto.getAdresse())
				.setLastModifiedDate(LocalDateTime.now()).setPhoto(null);

		if (oldUser.isPresent()) {
			UserMapper.userToUserDto(userRepository.save(persistedUser));
		} else {
			throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND,
					"Invalid user email:" + "" + userDto.getEmail());
		}
		return userDto;
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
		final Page<UserDto> uPage = new PageImpl<>(userDtos.subList(startItem, currentPage), of,
				users.getTotalElements());
		return uPage;
	}

	@Override
	public Page<UserDto> findByIdPageable(Long id, Pageable pageRequest) {

		Optional<UserDto> uOptional = Optional.ofNullable(UserMapper.userToUserDto(userRepository.findById(id).get()));
		List<UserDto> userDtos = uOptional.map(Collections::singletonList).orElse(emptyList());

		return new PageImpl<>(userDtos, pageRequest, userDtos.size());
	}

	@Override
	@Cacheable(value = "cache.byEmailContaining")
	public Page<UserDto> findByEmailContaining(String email, Pageable pageable) {

		Page<User> uPage = userRepository.findByEmailContainingOrderByIdAsc(email, pageable);
		List<UserDto> userDtos = uPage.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());

		return new PageImpl<>(userDtos, pageable, uPage.getTotalElements());
	}

	@Override
	public UserDto findByEmailAndIdNot(String email, Long id) {

		return modelMapper.map(userRepository.findByEmailAndIdNot(email, id), UserDto.class);
	}

	@Override
	public Page<UserDto> findByFirstNameContaining(String name, PageRequest pageRequest) {

		Page<User> uPage = userRepository.firstNameContaining(name, pageRequest);
		List<UserDto> userDtos = uPage.stream().map(user -> modelMapper.map(user, UserDto.class))
				.collect(Collectors.toList());
		return new PageImpl<>(userDtos, pageRequest, uPage.getTotalElements());
	}

	@Override
	public Set<RoleDto> getAssignedRoleSet(UserDto userDto) {

		Map<Long, RoleDto> assignedRoleMap = new HashedMap<Long, RoleDto>();
		Set<RoleDto> roleDtos = userDto.getRoles();
		log.debug("user assigne role is:{}",userDto.getRoles());
		for (RoleDto uroleDto : roleDtos) {

			assignedRoleMap.put(uroleDto.getId(), uroleDto);

		}

		Set<RoleDto> roleDtos2 = new HashSet<RoleDto>();
		Set<RoleDto> roleDtos3 = roleService.getAllRoles();

		for (RoleDto roleDto : roleDtos3) {
			if (assignedRoleMap.containsKey(roleDto.getId())) {
				roleDtos3.add(roleDto);
			} else {
				roleDtos2.add(null);
			}
		}
		return roleDtos2;

	}

	@Override
	public UserDto deleteUser(UserDto userDto) {
		User persistedUser = new User().setAdresse(userDto.getAdresse()).setBirthDate(userDto.getBirthDate())
				.setEmail(userDto.getEmail()).setFirstName(userDto.getFirstName()).setGender(userDto.getGender())
				.setLastName(userDto.getLastName()).setMatchingPassword(userDto.getMatchingPassword())
				.setMobileNumber(userDto.getMobileNumber()).setPassword(userDto.getPassword())
				.setFirstName(userDto.getFullName()).setCreatedDate(userDto.getCreatedDate())
				.setCreatedBy(userDto.getCreatedBy()).setAdresse(userDto.getAdresse())
				.setLastModifiedDate(LocalDateTime.now()).setPhoto(null);
		return UserMapper.userToUserDto(userRepository.deleteUserById(persistedUser.getId()));
	}

}
