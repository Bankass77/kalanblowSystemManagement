/*
 * package ml.kalanblowsystemmanagement.security;
 * 
 * import java.util.Collection; import java.util.Set; import
 * java.util.stream.Collectors;
 * 
 * import org.springframework.security.core.GrantedAuthority; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.security.core.userdetails.UserDetails;
 * 
 * import lombok.experimental.Accessors; import
 * ml.kalanblowsystemmanagement.model.User;
 * 
 * @Accessors(chain = true) public class KalanblowUserDetails implements
 * UserDetails {
 * 
 *//**
	* 
	*//*
		 * private static final long serialVersionUID = 1L; private final Long id;
		 * private final String password; private final String displayName; private
		 * final String email; private final Set<GrantedAuthority> authorities;
		 * 
		 * public KalanblowUserDetails(User user) { super(); this.id = user.getId();
		 * this.displayName = user.getFullName(); this.password = user.getPassword();
		 * this.email=user.getEmail(); this.authorities = user.getRoles().stream()
		 * .map(userRole -> new SimpleGrantedAuthority( "ROLE_" +
		 * userRole.getUserRoleName().getUserRole())) .collect(Collectors.toSet()); }
		 * 
		 * @Override public Collection<? extends GrantedAuthority> getAuthorities() {
		 * 
		 * return authorities; }
		 * 
		 * @Override public String getPassword() {
		 * 
		 * return password; }
		 * 
		 * @Override public String getUsername() {
		 * 
		 * return email; }
		 * 
		 * @Override public boolean isAccountNonExpired() {
		 * 
		 * return true; }
		 * 
		 * @Override public boolean isAccountNonLocked() {
		 * 
		 * return true; }
		 * 
		 * @Override public boolean isCredentialsNonExpired() {
		 * 
		 * return true; }
		 * 
		 * @Override public boolean isEnabled() {
		 * 
		 * return true; }
		 * 
		 * public String getDisplayName() { return displayName; } }
		 */