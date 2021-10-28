
package ml.kalanblowSystemManagement.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;

import ml.kalanblowSystemManagement.model.User;

@Component
public class UserMapper {

    public static UserDto userToUserDto(User user) {
        return new UserDto().setId(user.getId()).setEmail(user.getEmail())
                .setFirstName(user.getFirstName()).setLastName(user.getLastName())
                .setPassword(user.getPassword()).setMatchingPassword(user.getMatchingPassword())
                .setRoleDtos(user.getRoles().stream()
                        .map(roleDto -> new ModelMapper().map(roleDto, RoleDto.class))
                        .collect(Collectors.toSet()))
                .setMobileNumber(user.getMobileNumber()).setBirthDate(user.getBirthDate())
                .setGender(user.getGender()).setCreatedDate(user.getCreatedDate())
                .setLastModifiedDate(user.getLastModifiedDate()).setAdresse(user.getAdresse());

    }

   public  static List<UserDto> mapUserIntoDtos(Iterable<User> uIterable) {

        List<UserDto> dtos = new ArrayList<>();
        uIterable.forEach(e -> dtos.add(userToUserDto(e)));
        return dtos;
    }

   public  static Page<UserDto> mapUserPageIntoDtoPage(Pageable pageable, Page<User> source) {
        List<UserDto> dtos = mapUserIntoDtos(source.getContent());
        return new PageImpl<>(dtos, pageable, source.getTotalElements());

    }
}
