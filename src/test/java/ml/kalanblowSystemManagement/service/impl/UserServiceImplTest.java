package ml.kalanblowSystemManagement.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;
import ml.kalanblowSystemManagement.service.UserService;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	UserService userService;

	@BeforeEach
	void initUseCase() {

		userService = new UserServiceImpl(userRepository, roleRepository, modelMapper, passwordEncoder);
	}

	@Test
	void testSignup() {

		UserDto userDto = new UserDto().setAdmin(true).setEmail("admin3@example.com").setFirstName("admin3")
				.setLastName("admin3").setPassword(passwordEncoder.encode("Example2021!"))
				.setMobileNumber("0267842387");

		when(userService.signup(any(UserDto.class))).thenReturn(userDto);

		UserDto saveUser = userService.signup(userDto);
		assertAll("userDto", ()->assertThat(saveUser.getFullName()).isNotNull());
	}

}
