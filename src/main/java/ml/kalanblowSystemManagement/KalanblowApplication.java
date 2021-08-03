package ml.kalanblowSystemManagement;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;
import ml.kalanblowSystemManagement.service.UserService;

@SpringBootApplication
@Slf4j
public class KalanblowApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalanblowApplication.class, args);
	}

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner addNewRole(RoleRepository roleRepository, UserRepository userRepository,
			UserService userService) {
		return (args) -> {
			log.debug("CommandLineRunner start");

			Optional<User> user = userRepository.findUserByEmail("admin1@example.com");

			user.ifPresent(newUser -> {
				System.out.println("User in commaandLineRuner is {}:" + user.get().getId());
			});

			 // createrole in database
			Role adminRole = new Role().setUserRoleName(UserRole.ADMIN);
           //  roleRepository.save(adminRole);
			// Create new User with role

			/*
			 * Role role2 = new Role().setUserRoleName(UserRole.STUDENT);
			 * roleRepository.save(role2);
			 */
			UserDto adminDto = new UserDto();

			adminDto.setEmail("admin3@example.com");
			adminDto.setFirstName("admin3");
			adminDto.setLastName("admin3");
			adminDto.setPassword(passwordEncoder.encode("Example2021!"));
			adminDto.setMobileNumber("0256369645");
			adminDto.setAdmin(true).setBirthDate(LocalDate.now()).setMatchingPassword("Example2021&");
			adminDto.setRoleDtos(new HashSet<>(Arrays.asList(new ModelMapper().map(adminRole, RoleDto.class))));

			if (!userService.emailExist(adminDto.getEmail())) {
				System.out.println("adminDto is:" + adminDto);
				userService.signup(adminDto);
			}

		};
	}

}
