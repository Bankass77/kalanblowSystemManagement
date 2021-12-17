
package ml.kalanblowSystemManagement.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Table(
        name = "role")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(
        chain = true)
public class Role implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO)
    @Column(
            name = "role_id",
            unique = true,
            nullable = false)
    private Long id;

    @Column(
            name = "userRole",
            nullable = false,
            unique = true)
    @Enumerated(EnumType.STRING)
  
    @NotNull
    private UserRole userRoleName;

    @ManyToMany(
            mappedBy = "roles",
            fetch = FetchType.EAGER)
    @OnDelete(
            action = OnDeleteAction.CASCADE)
    private Set<User> users;
    
    @ManyToMany(
            fetch = FetchType.EAGER)
    @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    @Fetch(FetchMode.JOIN)
    private Set<Privilege> privileges;

}
