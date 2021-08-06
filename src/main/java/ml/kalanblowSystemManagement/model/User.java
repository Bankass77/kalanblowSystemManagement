package ml.kalanblowSystemManagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Table(name = "user", indexes = @Index(name = "idx_user_email", columnList = "email", unique = true))
@Entity
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", nullable = false, unique = true)
	private Long id;

	@Column(name = "email", unique = true, updatable = true)
	@NotNull
	@Size(min = 4, max = 30)
	private String email;

	@Column(name = "firstName")
	@NotNull
	private String firstName;

	@Column(name = "lastName")
	@NotNull
	private String lastName;

	@Column(name = "password")
	@NotNull
	private String password;

	@NotNull
	@Column(name = "matchingPassword")
	private String matchingPassword;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "birthDate")
	private LocalDate birthDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	public String getFullName() {
		return firstName != null ? firstName.concat(" ").concat(lastName) : "";
	}
}
