package ml.kalanblowSystemManagement.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;

public interface UserService {

	UserDto findUserById(Long id);

	Optional<UserDto> findUserByEmail(String email);

	UserDto changeUserEmail(String newEmail);

	Optional<UserDto> findUserByfirstNameAndLastName(String firstName, String lastName);

	Set<UserDto> findUserByLastName(String lastName);

	Optional<UserDto> findUserByRoles(UserRole name);

	UserDto signup(UserDto userDto);

	UserDto updateUserProfile(UserDto userDto);

	public void editUser(UserDto userDTO);

	UserDto changeUserPassword(String oldPassword, String newPassword);

	UserDto deleteUserById(Long id);

	UserDto deleteUserByEmail(String email);

	Set<UserDto> getAllUsers();

	Page<UserDto> listUserByPage(Pageable pageable);
	
	Page<UserDto> findAllPageableOrderByLastName(String queryString, Pageable pageable);
	
	 Page<UserDto> findAllPageable(PageRequest of);
	Set<UserDto> findUserbyMobileNumber(String mobileNumber);
	
	boolean emailExist(String email);
	void addUserLocation(User user, String ip);

}
