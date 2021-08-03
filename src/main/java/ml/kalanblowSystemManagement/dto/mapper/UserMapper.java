package ml.kalanblowSystemManagement.dto.mapper;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;

import ml.kalanblowSystemManagement.model.User;

@Component
public class UserMapper {

	public static UserDto userToUserDto(User user) {
		return new UserDto().setEmail(user.getEmail()).setFirstName(user.getFirstName()).setLastName(user.getLastName())
				.setPassword(user.getPassword()).setMatchingPassword(user.getMatchingPassword()).setRoleDtos(user.getRoles().stream()
						.map(roleDto -> new ModelMapper().map(roleDto, RoleDto.class))
						.collect(Collectors.toSet())).setMobileNumber(user.getMobileNumber()).setBirthDate(user.getBirthDate());

	}

}
