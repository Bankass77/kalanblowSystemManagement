package ml.kalanblowsystemmanagement.service;

import java.util.Optional;
import java.util.Set;

import ml.kalanblowsystemmanagement.dto.model.RoleDto;

public interface RoleService {
	
	Set<RoleDto> getAllRoles();
	
	RoleDto findByName (String name);
	
	Optional<RoleDto> findById(Long id);
	
	void saveOrUpdate (RoleDto roleDto);
	
	boolean checkIfRoleNameIsTaken(Set<RoleDto> allRoles, RoleDto roleDto, RoleDto persisteRoleDto);

}
