
package ml.kalanblowsystemmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowsystemmanagement.model.Role;
import ml.kalanblowsystemmanagement.model.UserRole;

@Repository(
        value = "roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleById(Long id);

    Role findRoleByUserRoleName(UserRole userRole);

}
