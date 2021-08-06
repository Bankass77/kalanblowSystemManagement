
package ml.kalanblowSystemManagement.controller.web.ui;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.exception.EntityType;
import ml.kalanblowSystemManagement.exception.ExceptionType;
import ml.kalanblowSystemManagement.exception.KalanblowSystemManagementException;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;
import ml.kalanblowSystemManagement.service.UserService;
import ml.kalanblowSystemManagement.service.impl.TestUtil;
import ml.kalanblowSystemManagement.service.impl.UserServiceImpl;

@SpringBootTest
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
@WebAppConfiguration
@Disabled
class AdminControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	UserService userService = new UserServiceImpl(userRepository, roleRepository, modelMapper, passwordEncoder, null,
			null, null);

	@BeforeEach
	void initUseCase() {

		userService = new UserServiceImpl(userRepository, roleRepository, modelMapper, passwordEncoder, null, null,
				null);
	}

	@Test
	void testSignup() throws Exception {

	}

	@Test
	public void findById_UserEntryNotFound_ShouldRender404View() throws Exception {
		when(userService.findUserById(1L)).thenThrow(new KalanblowSystemManagementException()
				.throwException(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, String.valueOf(1L)));

		mockMvc.perform(get("/editeUser/{id}", 1L)).andExpect(status().isNotFound()).andExpect(view().name("error"))
				.andExpect(forwardedUrl("error"));

		verify(userService, times(1)).findUserById(1L);
		verifyZeroInteractions(userService);
	}

	public void testSignup_shouldReturnPost() throws Exception {
		String title = TestUtil.createStringWithLength(101);
		String description = TestUtil.createStringWithLength(501);

		mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("firstName", "admin4")
				.param("lastName", "admin4").param("email", "admin4@example.com")
				.param("password", passwordEncoder.encode("ExaMple2021!")).param("mobileNumber", "0267354980")
				.param("matchinPassword", passwordEncoder.encode("ExaMple2021!"))
				.param("roles", "new HashSet<>(Arrays.asList(new RoleDto().setUserRoleName(UserRole.STAFF.name()))"))
				.andExpect(status().isOk()).andExpect(view().name("/signup")).andExpect(forwardedUrl("/"))
				.andExpect(model().attributeHasFieldErrors("firstName", "title"))
				.andExpect(model().attributeHasFieldErrors("lastName", "description"))
				.andExpect(model().attributeHasFieldErrors("email", "email"))
				.andExpect(model().attributeHasFieldErrors("password", "password"))
				.andExpect(model().attributeHasFieldErrors("firstName", "title"))
				.andExpect(model().attributeHasFieldErrors("mobileNumber", "mobileNumber"))
				.andExpect(model().attributeHasFieldErrors("matchingPassword", "matchingPassword"))
				.andExpect(model().attribute("fisrtName", hasProperty("firstName", is("admin4"))))
				.andExpect(model().attribute("lastName", hasProperty("lastName", is("admin4"))))
				.andExpect(model().attribute("email", hasProperty("email", is("admin4@example.com"))))
				.andExpect(model().attribute("password",
						hasProperty("password", is(passwordEncoder.encode("ExaMple2021!")))))
				.andExpect(model().attribute("mobileNumber", hasProperty("mobileNumber", is("0267354980"))))
				.andExpect(model().attribute("matchingPassword",
						hasProperty("matchingPassword", is(passwordEncoder.encode("ExaMple2021!")))));

		verifyZeroInteractions(userService);
	}

	@Test
	public void add_NewUserDtoEntry_ShouldAddUserDtoEntryAndRenderViewUserDtoEntryView() throws Exception {
		UserDto userDto = new UserDto()

				.setFirstName("admin4").setAdmin(true).setBirthDate(LocalDate.now()).setEmail("admin4@example.com")
				.setLastName("admin4").setMatchingPassword(passwordEncoder.encode("ExaMple2021!"))
				.setMobileNumber("0267354980").setPassword(passwordEncoder.encode("ExaMple2021!"))
				.setRoleDtos(new HashSet<>(Arrays.asList(new RoleDto().setUserRoleName(UserRole.STAFF.name()))));

		when(userService.signup(isA(UserDto.class))).thenReturn(userDto);

		mockMvc.perform(post("/signup").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("firstName", "admin4")
				.param("lastName", "admin4").param("email", "admin4@example.com")
				.param("password", passwordEncoder.encode("ExaMple2021!")).param("mobileNumber", "0267354980")
				.param("matchinPassword", passwordEncoder.encode("ExaMple2021!"))
				.param("roles", "new HashSet<>(Arrays.asList(new RoleDto().setUserRoleName(UserRole.STAFF.name()))"))

				.andExpect(status().isMovedTemporarily()).andExpect(view().name("redirect:/editeUser/{id}"))
				.andExpect(redirectedUrl("//editeUser/1")).andExpect(model().attribute("id", is("1")))
				.andExpect(flash().attribute("feedbackMessage", is("UserDto entry: title was added.")));

		ArgumentCaptor<UserDto> formObjectArgument = ArgumentCaptor.forClass(UserDto.class);
		verify(userService, times(1)).signup(formObjectArgument.capture());
		verifyNoMoreInteractions(userService);

		UserDto formObject = formObjectArgument.getValue();

		assertEquals(formObject.getEmail(), is("admin4@example.com"));
		assertNull(formObject.getFullName());
		assertEquals(formObject.getLastName(), is("admin4"));
	}
}
