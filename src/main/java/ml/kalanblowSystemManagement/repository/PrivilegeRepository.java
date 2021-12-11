package ml.kalanblowSystemManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowSystemManagement.model.Privilege;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.UserPrivilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(UserPrivilege userPrivilege);

    void delete(Privilege privilege);
    
    Privilege findPrivilegeByActionAndEntityAndRole(String action, String entity, Role role);
}
