package ml.kalanblowSystemManagement.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

@Service
public class KalanblowSystemManagementCustomService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserDto> userDto = userService.findUserByEmail(username);

		if (userDto.isPresent()) {
			Set<GrantedAuthority> authorities = getAuthority(userDto.get().getRoleDtos());

			return buildUserForAuthentication(userDto.get(), authorities);
		} else {
			throw new UsernameNotFoundException("User with email" + username + "does not exist");
		}

	}

	private UserDetails buildUserForAuthentication(UserDto userDto, Set<GrantedAuthority> authorities) {

		return new User(userDto.getEmail(), userDto.getPassword(), authorities);
	}

	private Set<GrantedAuthority> getAuthority(Set<RoleDto> roleDtos) {

		Set<GrantedAuthority> roles = new HashSet<>();
		roleDtos.forEach((role) -> {

			roles.add(new SimpleGrantedAuthority(role.getUserRoleName()));
		});
		return new HashSet<>(roles);
	}

}
