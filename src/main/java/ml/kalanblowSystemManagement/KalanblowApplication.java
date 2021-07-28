package ml.kalanblowSystemManagement;

import java.util.Arrays;
import java.util.HashSet;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.Role;
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

	
	
	
	@Bean
	public CommandLineRunner addNewRole( RoleRepository roleRepository, UserRepository userRepository, UserService userService) {
		return (args)->{
		log.debug("CommandLineRunner start");
			
		// create role in database
		Role adminRole = roleRepository.findRoleByUserRoleName(UserRole.ADMIN);
		
			/*
			 * if (adminRole == null) {
			 * 
			 * adminRole= new Role(); adminRole.setUserRoleName(UserRole.ADMIN);
			 * 
			 * roleRepository.save(adminRole); }
			 */
		
	  //Role studentRole = new Role();
	  
	 // studentRole= roleRepository.findRoleByUserRoleName(UserRole.STUDENT);
	  
			/*
			 * if(studentRole==null) { studentRole= new Role();
			 * studentRole.setUserRoleName(UserRole.STUDENT);
			 * roleRepository.save(studentRole); }
			 */	
	  
	  // Create new User with role
	  
	 // User admin= userRepository.findUserByEmail("admin1@example.com");
	  
	  
			/*
			 * if (admin ==null) { admin= new User();
			 * 
			 * admin.setEmail("admin1@example.com"); admin.setFirstName("admin1");
			 * admin.setLastName("admin1"); admin.setPassword("Example2021!");
			 * admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
			 * 
			 * userRepository.save(admin); }
			 * 
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
