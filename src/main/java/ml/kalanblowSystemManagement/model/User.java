package ml.kalanblowSystemManagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ml.kalanblowSystemManagement.annotation.EmailContraint;
import ml.kalanblowSystemManagement.annotation.FrenchPhoneConstraint;
import ml.kalanblowSystemManagement.annotation.PastLocalDate;
import ml.kalanblowSystemManagement.annotation.ValidPassword;
import ml.kalanblowSystemManagement.exception.NotExistingUser;
import ml.kalanblowSystemManagement.utils.GenderConverter;

@Entity
@Getter
@Setter
@Accessors(
		chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(
		value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(
		ignoreUnknown = true)
@Inheritance(
		strategy = InheritanceType.JOINED)
@DiscriminatorColumn(
		discriminatorType = DiscriminatorType.STRING,
		name = "USER_TYPE")
@NotExistingUser(groups = ValidationGroupTwo.class)
@Table(
		name = "user",
		indexes = @Index(
				name = "idx_user_email",
				columnList = "email",
				unique = true))
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(
			strategy = GenerationType.AUTO)
	@Column(
			name = "user_id",
			nullable = false,
			unique = true)
	private Long id;


	@Column(
			name = "firstName")
	@NotNull
	@Size(min = 1, max = 200, groups = ValidationGroupOne.class)
	private String firstName;

	@Column(
			name = "lastName")
	@NotNull
	@Size(min = 1, max = 200, groups = ValidationGroupOne.class)
	private String lastName;

	@Column(
			name = "password")
	@NotNull
	@ValidPassword
	private String password;

	@Lob
	@Column(
			name = "photo")
	private byte[] photo;

	@NotNull
	@Column(
			name = "matchingPassword")
	@ValidPassword
	private String matchingPassword;

	private int failedLoginAttempts;
    private boolean loginDisabled;
	
	@Column(
			name = "mobile_number")
	@FrenchPhoneConstraint
	private String mobileNumber;

	@Column(
			name = "birthday",
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@NotNull
	@PastLocalDate
	@Past(
			message = "Date input is invalid for a  birth date.")

	@DateTimeFormat(
			pattern = "dd/MM/yyyy")
	private LocalDate birthDate;

	@ManyToMany(
			fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = {
					@JoinColumn(
							name = "user_id")
			},
			inverseJoinColumns = {
					@JoinColumn(
							name = "role_id")
			})
	private Set<Role> roles = new HashSet<Role>();

	@ElementCollection(targetClass = Role.class)
	private Collection<? extends GrantedAuthority> authorities;

	@OneToMany(
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@Cache(
			usage = CacheConcurrencyStrategy.READ_WRITE)
	@JoinColumn(
			name = "id")
	private Set<Device> devices = new HashSet<>();

	public String getFullName() {
		return firstName != null ? firstName.concat(" ").concat(lastName) : "";
	}

	@PrePersist
	void onCreate() {
		this.setCreatedDate(LocalDateTime.now());
		this.setLastModifiedDate(LocalDateTime.now());
	}

	@PreUpdate
	void onUpdate() {
		this.setLastModifiedDate(LocalDateTime.now());
	}


	@Column(
			name = "email",
			unique = true,
			updatable = true)
	@NotNull
	@Size(
			min = 4,
			max = 30, groups = ValidationGroupOne.class)
	@EmailContraint
	private String email;


	@Column(
			name = "sexe")
	@NotNull(
			message = "Sex is required")
	@Convert(
			converter = GenderConverter.class)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(
			name = "createdDate",
			columnDefinition = "TIMESTAMP",
			insertable = true,
			updatable = false)
	@JsonFormat(
			pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonDeserialize(
			using = LocalDateTimeDeserializer.class)
	@CreatedDate
	@DateTimeFormat(
			pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime createdDate;

	@Column(
			name = "lastModifiedDate",
			columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP",
			nullable = false,
			updatable = false)
	@JsonFormat(
			pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonDeserialize(
			using = LocalDateTimeDeserializer.class)
	@LastModifiedDate
	@DateTimeFormat(
			pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime lastModifiedDate;

	@CreatedBy
	@Column(
			columnDefinition = "bigint default 1",
			updatable = false)
	protected Long createdBy;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(
				name = "street",
				column = @Column(
						name = "street")),

		@AttributeOverride(
				name = "streetNumber",
				column = @Column(
						name = "streetNumber")),

		@AttributeOverride(
				name = "codePostal",
				column = @Column(
						name = "codePostal")),

		@AttributeOverride(
				name = "city",
				column = @Column(
						name = "city")),

		@AttributeOverride(
				name = "state",
				column = @Column(
						name = "state")),

		@AttributeOverride(
				name = "country",
				column = @Column(
						name = "country"))
	})
	private Addresse adresse;


	@Transient
	public boolean hasAnyRoles(String... roles) {
		return hasAnyRoles(Arrays.asList(roles));
	}

	@Transient
	public boolean hasAnyRoles(List<String> roles) {
		Set<UserRole> _roles = this.getRoles()
				.stream()
				.map(Role::getUserRoleName)
				.collect(Collectors.toSet());
		Sets.SetView<String> intersection = Sets.intersection(Sets.newHashSet(roles), _roles);
		return !intersection.isEmpty();
	}

	@Transient
	public boolean hasRoles(String... roles) {
		return hasRoles(Arrays.asList(roles));
	}

	@Transient
	public boolean hasRoles(List<String> roles) {
		Set<UserRole> _roles = this.getRoles()
				.stream()
				.map(Role::getUserRoleName)
				.collect(Collectors.toSet());
		return _roles.containsAll(roles);
	}

	@Transient
	public boolean hasAnyPrivileges(String... privileges) {
		return hasAnyPrivileges(Arrays.asList(privileges));
	}

	@Transient
	public boolean hasAnyPrivileges(List<String> privileges) {
		Set<UserPrivilege> _privileges = this.getRoles()
				.stream()
				.flatMap(s -> s.getPrivileges().stream())
				.map(Privilege::getName)
				.collect(Collectors.toSet());
		Set<String> setOfPrivSet=_privileges.stream().map(UserPrivilege:: name).collect(Collectors.toSet());
		Sets.SetView<String> intersection = Sets.intersection(setOfPrivSet, Sets.newHashSet(privileges.iterator()));
		return !intersection.isEmpty();
	}

	@Transient
	public boolean hasPrivileges(String... privileges) {
		return hasPrivileges(Arrays.asList(privileges));
	}

	@Transient
	public boolean hasPrivileges(List<String> privileges) {
		Set<UserPrivilege> _privileges = this.getRoles()
				.stream()
				.flatMap(s -> s.getPrivileges().stream())
				.map(Privilege::getName)
				.collect(Collectors.toSet());
		return _privileges.containsAll(privileges);
	}
	
	
}
