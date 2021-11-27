
package ml.kalanblowsystemmanagement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowsystemmanagement.dto.model.RoleDto;
import ml.kalanblowsystemmanagement.dto.model.UserDto;
import ml.kalanblowsystemmanagement.exception.KalanblowSystemManagementException;
import ml.kalanblowsystemmanagement.model.Addresse;
import ml.kalanblowsystemmanagement.model.Gender;
import ml.kalanblowsystemmanagement.model.Role;
import ml.kalanblowsystemmanagement.model.User;
import ml.kalanblowsystemmanagement.model.UserRole;
import ml.kalanblowsystemmanagement.repository.RoleRepository;
import ml.kalanblowsystemmanagement.repository.UserRepository;
import ml.kalanblowsystemmanagement.service.UserService;

@SpringBootApplication
@Slf4j
// @EnableJpaAuditing
// @EnableAspectJAutoProxy(proxyTargetClass = true)
public class KalanblowApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalanblowApplication.class, args);

	}
	/*
	 * @Autowired private BCryptPasswordEncoder passwordEncoder;
	 * 
	 * @Bean public CommandLineRunner addNewRole(RoleRepository roleRepository,
	 * UserRepository userRepository, UserService userService) { return (args) -> {
	 * log.debug("CommandLineRunner start");
	 * 
	 * Optional<User> user = userRepository.findByEmail("admin@example.com");
	 * 
	 * user.ifPresent(newUser -> {
	 * System.out.println("User in commaandLineRuner is {}:" + user.get().getId());
	 * });
	 * 
	 * // create a role in database Role adminRole = new
	 * Role().setUserRoleName(UserRole.STAFF); // roleRepository.save(adminRole); //
	 * Create // new User with role
	 * 
	 * // Role adminRole2 = new Role().setUserRoleName(UserRole.ADMIN); // //
	 * roleRepository.save(adminRole2); // Create new User with role
	 * 
	 * // Role studentRole = new Role().setUserRoleName(UserRole.STUDENT); //
	 * //roleRepository.save(studentRole); // Create new User with // Role role2 =
	 * new Role().setUserRoleName(UserRole.TEACHER); // //
	 * roleRepository.save(role2);
	 * 
	 * boolean emailExist = userService.emailExist("admin3@example.com");
	 * 
	 * if (!emailExist) { throw new
	 * KalanblowSystemManagementException.DuplicateEntityException("duplicate email"
	 * ); } else { UserDto adminDto = new UserDto();
	 * 
	 * adminDto.setEmail("admin3@example.com"); adminDto.setFirstName("admin3");
	 * adminDto.setLastName("admin3");
	 * adminDto.setPassword(passwordEncoder.encode("ExaMple2021!"));
	 * adminDto.setMobileNumber("0256369649");
	 * adminDto.setAdmin(true).setBirthDate(LocalDate.now())
	 * .setMatchingPassword(passwordEncoder.encode("ExaMple2021!"));
	 * adminDto.setRoles(new HashSet<>(Arrays.asList(new
	 * ModelMapper().map(adminRole, RoleDto.class))));
	 * adminDto.setGender(Gender.FEMALE);
	 * adminDto.setCreatedDate(LocalDateTime.now());
	 * adminDto.setLastModifiedDate(LocalDateTime.now()); adminDto.setAdmin(true);
	 * adminDto.setBirthDate(LocalDate.of(1990, Month.APRIL, 5)); Addresse addresse
	 * = new Addresse(); addresse.setCity("Paris"); addresse.setCodePostale(75012);
	 * addresse.setCountry("France"); addresse.setState("Ile-De-France");
	 * adminDto.setCreatedBy(5L); addresse.setStreet("Rue Robert");
	 * addresse.setStreetNumber(35); adminDto.setAdresse(addresse);
	 * System.out.println("adminDto is:" + adminDto); userService.signup(adminDto);
	 * }
	 * 
	 * }; }
	 */
}