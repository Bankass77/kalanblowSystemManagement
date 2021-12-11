
package ml.kalanblowSystemManagement.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

import static java.lang.String.format;

@Component(value = "kalanblowSystemManagementCustomService")
@Transactional
public class KalanblowSystemManagementCustomService implements UserDetailsService {

	private final UserService userService;


	@Autowired
	public KalanblowSystemManagementCustomService(UserService userService) {
		super();
		this.userService = userService;
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserDto> userDto = Optional.ofNullable(userService.findUserByEmail(username).orElseThrow(
				() -> new UsernameNotFoundException(format("Admin with email %s could not be found", username))));


		if (userDto != null) {
			Set<GrantedAuthority> authorities = getAuthority(userDto.get().getRoles());

			return buildUserForAuthentication(userDto.get(), authorities);
		} else {
			throw new UsernameNotFoundException("Admin with email" + username + "does not exist");
		}

	}

	boolean enabled = true;
	boolean accountNonExpired = true;
	boolean credentialsNonExpired = true;
	boolean accountNonLocked = true;

	private UserDetails buildUserForAuthentication(UserDto userDto, Set<GrantedAuthority> authorities) {

		return new User(userDto.getEmail(), userDto.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
	}

	private Set<GrantedAuthority> getAuthority(Set<RoleDto> roleDtos) {

		Set<GrantedAuthority> roles = new HashSet<>();
		roleDtos.forEach((role) -> {

			roles.add(new SimpleGrantedAuthority("ROLE_" +role.getUserRoleName()));
			
			 
		});
		return new HashSet<>(roles);
	}

}
