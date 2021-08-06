
package ml.kalanblowSystemManagement.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import ml.kalanblowSystemManagement.dto.mapper.UserMapper;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;
import ml.kalanblowSystemManagement.service.UserService;
import static org.assertj.core.api.Java6Assertions.assertThat; 
import java.time.LocalDate;

@SpringBootTest

@ExtendWith(MockitoExtension.class)

//@Disabled

@AutoConfigureMockMvc

@ActiveProfiles("test")

@DisplayName("UserServiceImplTest test case")

@Transactional
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	UserService userService = new UserServiceImpl(userRepository, roleRepository, modelMapper, passwordEncoder, null,
			null, null);

	@BeforeEach
	void initUseCase() {

		userService = new UserServiceImpl(userRepository, roleRepository, modelMapper, passwordEncoder, null, null,
				null);
	}

	@Test
	void testSignup() {

		User user = new User().setEmail("admin2@example.com").setFirstName("admin2")
				.setLastName("admin2").setPassword(passwordEncoder.encode("ExaMple2021!"))
				.setMatchingPassword(passwordEncoder.encode("ExaMple2021!")).setMobileNumber("0267842387")
				.setBirthDate(LocalDate.now());
        UserDto newUser=UserMapper.userToUserDto(user);
		if (!userService.emailExist(newUser.getEmail())) {
			System.out.println("email exist in db.");
		}else {
			UserDto saveUser = userService.signup(newUser);
			assertAll("userDto", () -> assertThat(saveUser.getFullName()).isNotNull());
		}
		
	}

}
