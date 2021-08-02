package ml.kalanblowSystemManagement;

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
import org.springframework.ui.ModelMap;

import lombok.extern.slf4j.Slf4j;
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

			// create role in database
		//	Role adminRole = roleRepository.findRoleByUserRoleName(UserRole.ADMIN);

			/*
			 * if (adminRole == null) {
			 * 
			 * adminRole= new Role(); adminRole.setUserRoleName(UserRole.ADMIN);
			 * 
			 * roleRepository.save(adminRole); }
			 */

			// Role studentRole = new Role();

			// studentRole= roleRepository.findRoleByUserRoleName(UserRole.STUDENT);

			/*
			 * if(studentRole==null) { studentRole= new Role();
			 * studentRole.setUserRoleName(UserRole.STUDENT);
			 * roleRepository.save(studentRole); }
			 */

			// Create new User with role

			/*
			 * UserDto adminDto = userService.findUserByEmail("admin3@example.com");
			 * 
			 * if (adminDto ==null) { adminDto = new UserDto();
			 * 
			 * adminDto.setEmail("admin3@example.com"); adminDto.setFirstName("admin3");
			 * adminDto.setLastName("admin3");
			 * adminDto.setPassword(passwordEncoder.encode("Example2021!"));
			 * adminDto.setMobileNumber("0256369645"); adminDto.setRoleDtos(new
			 * HashSet<>(Arrays .asList(new ModelMapper().map(adminRole, RoleDto.class))));
			 * 
			 * System.out.println("adminDto is:" + adminDto); userService.signup(adminDto);
			 * }
			 */ /*
				 * else {
				 * 
				 * new KalanblowSystemManagementException(); throw
				 * KalanblowSystemManagementException.throwException(EntityType.USER,
				 * ExceptionType.DUPLICATE_ENTITY, adminDto.getEmail()+
				 * "exist in database"); }
				 */
			/*
			 * UserDto userDto = new UserDto();
			 * userDto.setAdmin(true).setEmail("admin2@example.com").setFirstName("admin2").
			 * setLastName("admin2") .setPassword("Example2021!").setRoleDtos(new
			 * HashSet<>(Arrays.asList(new ModelMapper().map(adminRole, RoleDto.class))));
			 * userService.signup(userDto);
			 */
		};
	}
}
