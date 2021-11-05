
package ml.kalanblowSystemManagement.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;

public interface UserService {

    UserDto findUserById(Long id);

    Page<UserDto> findByIdPageable(Long id, Pageable pageRequest);

    public Page<UserDto> findByEmailContaining(String email, Pageable pageable);

    UserDto findByEmailAndIdNot(String email, Long id);

    Optional<UserDto> findUserByEmail(String email);

    UserDto changeUserEmail(String newEmail);

    boolean emailExist(String email);

    Page<UserDto> findByFirstNameContaining(String name, PageRequest pageRequest);

    Optional<UserDto> findUserByfirstNameAndLastName(String firstName, String lastName);

    Set<UserDto> findUserByLastName(String lastName);

    Optional<UserDto> findUserByRoles(UserRole name);

    UserDto signup(UserDto userDto);

    UserDto updateUserProfile(UserDto userDto);

    void editUser(UserDto userDTO);

    UserDto changeUserPassword(String oldPassword, String newPassword);

    UserDto deleteUserById(Long id);

    UserDto deleteUser(UserDto userDto);

    UserDto deleteUserByEmail(String email);

    Set<UserDto> getAllUsers();

    Page<UserDto> listUserByPage(Pageable pageable);

    Page<UserDto> findAllPageableOrderByLastName(String queryString, Pageable pageable);

    Page<UserDto> findAllPageable(PageRequest of);

    Set<UserDto> findUserbyMobileNumber(String mobileNumber);

    void addUserLocation(User user, String ip);

    Set<RoleDto> getAssignedRoleSet(UserDto userDto);

}
