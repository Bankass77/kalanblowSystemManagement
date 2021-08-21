package ml.kalanblowSystemManagement.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ml.kalanblowSystemManagement.annotation.EmailContraint;
import ml.kalanblowSystemManagement.annotation.FrenchPhoneConstraint;
import ml.kalanblowSystemManagement.annotation.PastLocalDate;
import ml.kalanblowSystemManagement.annotation.ValidPassword;
import ml.kalanblowSystemManagement.utils.GenderConverter;

@Table(name = "user", indexes = @Index(name = "idx_user_email", columnList = "email", unique = true))
@Entity
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "USER_TYPE")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", nullable = false, unique = true)
	private Long id;

	@Column(name = "email", unique = true, updatable = true)
	@NotNull
	@Size(min = 4, max = 30)
	@EmailContraint
	private String email;

	@Column(name = "firstName")
	@NotNull
	private String firstName;

	@Column(name = "lastName")
	@NotNull
	private String lastName;

	@Column(name = "password")
	@NotNull
	@ValidPassword
	private String password;
	
	@Lob
	@Column(name = "photo")
	private byte[] photo;

	@NotNull
	@Column(name = "matchingPassword")
	@ValidPassword
	private String matchingPassword;

	@Column(name = "mobile_number")
	@FrenchPhoneConstraint
	private String mobileNumber;

	@Column(name = "birthday", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@NotNull
	@PastLocalDate
	@Past(message = "Date input is invalid for a  birth date.")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JoinColumn(name = "id")
	private Set<Device> devices = new HashSet<>();

	@Column(name = "sexe")
	@NotNull
	@Convert(converter = GenderConverter.class)
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "createdDate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:MM:SS")
	@JsonFormat(pattern = "dd/MM/yyyy HH:MM:SS")
	@CreationTimestamp
	private LocalDateTime createdDate;

	@Column(name = "lastModifiedDate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:MM:SS")
	@UpdateTimestamp
	@JsonFormat(pattern = "dd/MM/yyyy HH:MM:SS")
	private LocalDateTime lastModifiedDate;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "street")),

			@AttributeOverride(name = "streetNumber", column = @Column(name = "streetNumber")),

			@AttributeOverride(name = "codePostal", column = @Column(name = "codePostal")),

			@AttributeOverride(name = "city", column = @Column(name = "city")),

			@AttributeOverride(name = "state", column = @Column(name = "state")) })
	private Adresse adresse;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "street_pro")),

			@AttributeOverride(name = "streetNumber", column = @Column(name = "streetNumber_pro")),

			@AttributeOverride(name = "codePostal", column = @Column(name = "codePostal_pro")),

			@AttributeOverride(name = "city", column = @Column(name = "city_pro")),

			@AttributeOverride(name = "state", column = @Column(name = "state_pro")) })
	private Adresse adressePro;

	public String getFullName() {
		return firstName != null ? firstName.concat(" ").concat(lastName) : "";
	}
}
