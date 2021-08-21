package ml.kalanblowSystemManagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class KalanblowApplication {
	
	

	public static void main(String[] args) {
		SpringApplication.run(KalanblowApplication.class, args);


		
	}
	@Autowired
	private WebApplicationContext webAppContext;
	private final static LocalDateTime startDateTime = LocalDateTime.now();
	private final static DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy h:mm a");

	@RequestMapping("/server")
	@ResponseBody
	public String infoTagServer(){
	return new StringJoiner("<br>")
	.add("-------------------------------------")
	.add(" Server: "+
	webAppContext.getServletContext().getServerInfo())
	.add(" Start date: "+
	startDateTime.format(DT_FORMATTER))
	.add(" Version: " +
	webAppContext.getBean("webAppVersion")).add("--------------------------------------")
	.toString();
	}
	
	/*
	 * @Autowired private BCryptPasswordEncoder passwordEncoder;
	 * 
	 * @Bean public CommandLineRunner addNewRole(RoleRepository roleRepository,
	 * UserRepository userRepository, UserService userService) { return (args) -> {
	 * log.debug("CommandLineRunner start");
	 * 
	 * Optional<User> user = userRepository.findByEmail("admin1@example.com");
	 * 
	 * user.ifPresent(newUser -> {
	 * System.out.println("User in commaandLineRuner is {}:" + user.get().getId());
	 * });
	 * 
	 * // create a role in database Role adminRole = new
	 * Role().setUserRoleName(UserRole.STAFF); //roleRepository.save(adminRole); //
	 * Create new User with role
	 * 
	 * Role role2 = new Role().setUserRoleName(UserRole.TEACHER);
	 * //roleRepository.save(role2);
	 * 
	 * boolean emailExist = userService.emailExist("admin4@example.com");
	 * 
	 * if (!emailExist) { throw new
	 * KalanblowSystemManagementException.DuplicateEntityException("duplicate email"
	 * ); } else { UserDto adminDto = new UserDto();
	 * 
	 * adminDto.setEmail("admin4@example.com"); adminDto.setFirstName("admin4");
	 * adminDto.setLastName("admin4");
	 * adminDto.setPassword(passwordEncoder.encode("ExaMple2021!"));
	 * adminDto.setMobileNumber("0256369645");
	 * adminDto.setAdmin(true).setBirthDate(LocalDate.now())
	 * .setMatchingPassword(passwordEncoder.encode("ExaMple2021&"));
	 * adminDto.setRoleDtos(new HashSet<>(Arrays.asList(new
	 * ModelMapper().map(adminRole, RoleDto.class))));
	 * 
	 * System.out.println("adminDto is:" + adminDto); userService.signup(adminDto);
	 * }
	 * 
	 * }; }
	 */

}
