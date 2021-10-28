
package ml.kalanblowSystemManagement.dto.mapper;

import org.springframework.stereotype.Component;

import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.model.Role;

@Component
public class RoleMapper {

    public static RoleDto roleToRoleDto(Role role) {
        return new RoleDto().setId(role.getId())
                .setUserRoleName(role.getUserRoleName().getUserRole());

    }

}
