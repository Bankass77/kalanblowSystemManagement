
package ml.kalanblowsystemmanagement.dto.mapper;

import org.springframework.stereotype.Component;

import ml.kalanblowsystemmanagement.dto.model.RoleDto;
import ml.kalanblowsystemmanagement.model.Role;

@Component
public class RoleMapper {

    public static RoleDto roleToRoleDto(Role role) {
        return new RoleDto().setId(role.getId())
                .setUserRoleName(role.getUserRoleName().getUserRole());

    }

}
