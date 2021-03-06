
package ml.kalanblowSystemManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import lombok.extern.slf4j.Slf4j;

/**
 * @author a.guindo
 *
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j 
@EnableCaching
public class KalanblowApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalanblowApplication.class, args);

	}


	/*
	 * @Autowired private BCryptPasswordEncoder passwordEncoder;
	 * 
	 * @Autowired private PrivilegeRepository privilegeRepository;
	 * 
	 * @Autowired private RoleRepository roleRepository;
	 * 
	 * @Bean public CommandLineRunner addNewRole( UserRepository userRepository,
	 * UserService userService) { return (args) -> {
	 * log.debug("CommandLineRunner start");
	 * 
	 * Optional<User> user = userRepository.findByEmail("admin@example.com");
	 * 
	 * user.ifPresent(newUser -> {
	 * System.out.println("User in commaandLineRuner is {}:" + user.get().getId());
	 * });
	 * 
	 * // == create initial privileges //Privilege readPrivilege =
	 * //createPrivilegeIfNotFound(UserPrivilege.READ); //Privilege writePrivilege =
	 * //createPrivilegeIfNotFound(UserPrivilege.WRITRE);
	 * 
	 * // == create initial roles //List<Privilege> adminPrivileges
	 * =Arrays.asList(readPrivilege, writePrivilege);
	 * 
	 * // create a role in database //Role adminRole = new
	 * Role().setUserRoleName(UserRole.STAFF); // //roleRepository.save(adminRole);
	 * //Create // new User with role
	 * 
	 * // Role adminRole2 = new Role().setUserRoleName(UserRole.ADMIN); // //
	 * List<Privilege> adminPrivileges= privilegeRepository.findAll();
	 * adminRole2.setPrivileges(new HashSet<>(adminPrivileges));
	 * //roleRepository.save(adminRole2); // Create new User with role
	 * 
	 * // Role studentRole = new Role().setUserRoleName(UserRole.STUDENT);
	 * //List<Privilege>
	 * studentPrivilege=Arrays.asList(privilegeRepository.findByName(UserPrivilege.
	 * READ)); //studentRole.setPrivileges(new HashSet<>(studentPrivilege));
	 * //roleRepository.save(studentRole); // Create new User with //Role role2 =new
	 * Role().setUserRoleName(UserRole.TEACHER); //role2.setPrivileges(new
	 * HashSet<>(adminPrivileges));
	 * 
	 * //roleRepository.save(role2);
	 * 
	 * 
	 * //Role userRole =new Role().setUserRoleName(UserRole.USER);
	 * //userRole.setPrivileges(new HashSet<>(studentPrivilege));
	 * //roleRepository.save(userRole);
	 * 
	 * boolean emailExist = userService.emailExist("admin2@example.com");
	 * 
	 * if (!emailExist) { throw new
	 * KalanblowSystemManagementException.DuplicateEntityException("duplicate email"
	 * ); } else { UserDto adminDto = new UserDto();
	 * 
	 * adminDto.setEmail("admin2@example.com"); adminDto.setFirstName("admin2");
	 * adminDto.setLastName("admin");
	 * adminDto.setPassword(passwordEncoder.encode("ExaMple2021!"));
	 * adminDto.setMobileNumber("0256369649");
	 * adminDto.setAdmin(true).setBirthDate(LocalDate.now())
	 * .setMatchingPassword(passwordEncoder.encode("ExaMple2021!"));
	 * adminDto.setRoles(new HashSet<>(Arrays.asList(new
	 * ModelMapper().map(adminRole2, RoleDto.class))));
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
	 * };
	 * 
	 * 
	 * }
	 * 
	 * @Transactional public Privilege createPrivilegeIfNotFound(UserPrivilege
	 * userPrivilege) { Privilege privilege =
	 * privilegeRepository.findByName(userPrivilege); if (privilege == null) {
	 * privilege = new Privilege(null, UserPrivilege.valueOf(userPrivilege.name()),
	 * null); privilegeRepository.save(privilege); } return privilege; }
	 */

}
