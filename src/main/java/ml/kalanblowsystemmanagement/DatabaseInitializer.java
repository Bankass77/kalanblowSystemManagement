/*
 * package ml.kalanblowsystemmanagement;
 * 
 * import java.time.LocalDate; import java.time.LocalDateTime; import
 * java.time.ZoneId; import java.util.HashSet; import java.util.Set;
 * 
 * import org.springframework.boot.CommandLineRunner; import
 * org.springframework.context.annotation.Profile; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.stereotype.Component;
 * 
 * import com.github.javafaker.Faker;
 * 
 * import lombok.extern.slf4j.Slf4j; import
 * ml.kalanblowsystemmanagement.dto.model.RoleDto; import
 * ml.kalanblowsystemmanagement.dto.model.UserDto; import
 * ml.kalanblowsystemmanagement.model.Addresse; import
 * ml.kalanblowsystemmanagement.model.Gender; import
 * ml.kalanblowsystemmanagement.model.UserRole; import
 * ml.kalanblowsystemmanagement.service.RoleService; import
 * ml.kalanblowsystemmanagement.service.UserService;
 * 
 * @Component
 * 
 * @Profile({ "dev", "default" })
 * 
 * @Slf4j public class DatabaseInitializer implements CommandLineRunner {
 * private final Faker faker = new Faker(); // <.> private final UserService
 * userService; private final RoleService roleService; private final
 * BCryptPasswordEncoder bCryptPasswordEncoder;
 * 
 * public DatabaseInitializer(UserService userService, BCryptPasswordEncoder
 * bCryptPasswordEncoder, RoleService roleService) { // <.> this.userService =
 * userService; this.bCryptPasswordEncoder = bCryptPasswordEncoder;
 * this.roleService = roleService; }
 * 
 * @Override public void run(String... args) { for (int i = 0; i < 20; i++) { //
 * <.> UserDto userDto= newRandomUserParameters(); userService.signup(userDto);
 * } }
 * 
 * private UserDto newRandomUserParameters() { UserDto userDto = new UserDto();
 * 
 * String fullName = faker.name().fullName(); String firtName =
 * fullName.substring(fullName.indexOf(' ')); String lastName =
 * fullName.substring(0, fullName.length());
 * 
 * userDto.setFirstName(firtName); userDto.setLastName(lastName);
 * 
 * Gender gender = faker.bool().bool() ? Gender.MALE : Gender.FEMALE;
 * 
 * userDto.setGender(gender);
 * 
 * String city = faker.address().city(); String streetAdres =
 * faker.address().streetAddress(); String state = faker.address().state();
 * String zipCode = faker.address().zipCode(); String country =
 * faker.address().country();
 * 
 * Addresse addresse = new Addresse(); try {
 * 
 * addresse.setCity(city); addresse.setCountry(country);
 * addresse.setState(state); addresse.setStreet(streetAdres);
 * addresse.setCodePostale(Integer.valueOf(zipCode));
 * userDto.setAdresse(addresse); } catch (NumberFormatException e) {
 * log.debug(e.getMessage()); }
 * 
 * LocalDate birthday = LocalDate.ofInstant(faker.date().birthday(6,
 * 18).toInstant(), ZoneId.systemDefault()); userDto.setBirthDate(birthday);
 * 
 * String email = faker.internet().emailAddress(fullName);
 * userDto.setEmail(email);
 * 
 * userDto.setMobileNumber(faker.phoneNumber().phoneNumber()); String password =
 * faker.lorem().characters(8, 12);
 * userDto.setPassword(bCryptPasswordEncoder.encode(password));
 * 
 * userDto.setMatchingPassword(bCryptPasswordEncoder.encode(password));
 * 
 * RoleDto roplDtos = roleService.findByName(UserRole.ADMIN.getUserRole());
 * 
 * Set<RoleDto> roleDtos = new HashSet<RoleDto>();
 * 
 * roleDtos.add(roplDtos); userDto.setRoles(roleDtos);
 * 
 * userDto.setCreatedDate(LocalDateTime.now());
 * 
 * userDto.setLastModifiedDate(LocalDateTime.now());
 * 
 * userDto.setAdmin(true); return userDto; } }
 */