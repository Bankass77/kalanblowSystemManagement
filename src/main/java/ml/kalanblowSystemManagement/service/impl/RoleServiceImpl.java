
package ml.kalanblowSystemManagement.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.dto.mapper.RoleMapper;
import ml.kalanblowSystemManagement.dto.model.PrivilegeDto;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.model.Privilege;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.UserRole;
import ml.kalanblowSystemManagement.repository.PrivilegeRepository;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.service.RoleService;

@Component(
        value = "roleService")
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;
    
    private PrivilegeRepository privilegeRepository;

    private ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper,PrivilegeRepository privilegeRepository) {
        super();
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.privilegeRepository= privilegeRepository;
    }

    @Override
    @Cacheable("cache.allRoles")
    public Set<RoleDto> getAllRoles() {

        List<Role> roles = roleRepository.findAll();
      //  List<Privilege> privileges=privilegeRepository.findAll();
        log.debug("roles:{}", roles);
        List<RoleDto> roleDtos =
                roles.stream().map(roleDtO -> new ModelMapper().map(roleDtO, RoleDto.class))
                        .collect(Collectors.toList());
      //  List<PrivilegeDto> privilegeDtos=privileges.stream().map(p-> new ModelMapper().map(p, PrivilegeDto.class)).collect(Collectors.toList());
        
         
        return new HashSet<>(roleDtos);
    }

    @Override
    @Cacheable(
            value = "cache.roleByName",
            key = "#name",
            unless = "#result == null")
    public RoleDto findByName(String name) {

        Role role = roleRepository.findRoleByUserRoleName(UserRole.valueOf(name));
        RoleDto roleDto = RoleMapper.roleToRoleDto(role);
        return roleDto;
    }

    @Override
    @Cacheable(
            value = "cache.roleById",
            key = "#id",
            unless = "#result == null")
    public Optional<RoleDto> findById(Long id) {

        Optional<Role> role = roleRepository.findById(id);
        return Optional.ofNullable(modelMapper.map(role, RoleDto.class));
    }

    @Override
    @CacheEvict(
            value = {
                "cache.allRoles", "cache.roleByName", "cache.roleById"
            },
            allEntries = true)
    public void saveOrUpdate(RoleDto roleDto) {
        roleRepository.save(modelMapper.map(roleDto, Role.class));

    }

    @Override
    public boolean checkIfRoleNameIsTaken(Set<RoleDto> allRoles, RoleDto roleDto,
            RoleDto persisteRoleDto) {

        boolean roleNameAlreadyExists = false;

        for (RoleDto roleDto2 : allRoles) {

            // check if the role name is edited and if it is taken

            if (!roleDto.getUserRoleName().equals(persisteRoleDto.getUserRoleName())
                    && roleDto.getUserRoleName().equals(roleDto2.getUserRoleName())) {
                roleNameAlreadyExists= true;

            }

        }
        return roleNameAlreadyExists;
    }

}
