package ml.kalanblowSystemManagement.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.UserRole;

public interface UserService {

	Optional<UserDto> findUserById(Long id);

	Optional<UserDto> findUserByEmail(String email);

	Optional<UserDto> findUserByfirstNameAndLastName(String firstName, String lastName);

	Optional<UserDto> findUserByRoles(UserRole name);

	UserDto signup(UserDto userDto);

	UserDto updateUserProfile(UserDto userDto);

	UserDto changeUserPassword(UserDto userDto, String newPassword);

	UserDto deleteUserById(Long id);

	UserDto deleteUserByEmail(String email);

	Set<UserDto> getAllUsers();
	
	Page<UserDto> listUserByPage(Pageable pageable);

}
