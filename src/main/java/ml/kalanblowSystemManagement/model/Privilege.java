package ml.kalanblowSystemManagement.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Table(
		name = "privilege")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(
		chain = true)
public class Privilege implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(
			name = "privilege_id",
			nullable = false,
			unique = true)
	private Long id;

	   @Column(length = 20) 
	    private String action;

	    @Column(length = 40)
	    private String entity;

	    @Column
	    private Boolean constrained;
	@Column(
			name = "name",
			nullable = false,
			unique = true)
	@Enumerated(EnumType.STRING)
	@NotNull
	private UserPrivilege name;

	@ManyToOne
	private Role role;

	public Boolean isConstrained() {
		return constrained;
	}

	public void setIsConstrained(Boolean isConstrained){
		constrained = isConstrained;
	}

}
