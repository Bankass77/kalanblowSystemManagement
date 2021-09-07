
package ml.kalanblowSystemManagement.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.repository.RoleRepository;
import ml.kalanblowSystemManagement.service.RoleService;

@Component(
        value = "roleService")
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        super();
        this.roleRepository = roleRepository;
    }

    @Override
    public Set<RoleDto> getAllRoles() {

        List<Role> roles = roleRepository.findAll();
        log.debug("roles:{}", roles);
        List<RoleDto> roleDtos =
                roles.stream().map(roleDtO -> new ModelMapper().map(roleDtO, RoleDto.class))
                        .collect(Collectors.toList());

        return new HashSet<>(roleDtos);
    }

}
