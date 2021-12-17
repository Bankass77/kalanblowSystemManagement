
package ml.kalanblowSystemManagement.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Accessors(
		chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(
		name = "persistent_logins",
		indexes = @Index(
				name = "idx_persistentLogin_series",
				columnList = "series",
				unique = true))
@Setter
@Getter
@JsonInclude(
		value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(
		ignoreUnknown = true)
public class PersistentLogin implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Size(
			max = 50)
	private String series;
	@Column(
			length = 39)

	private String username;

	@NotNull
	@Size(
			max = 24)
	private String token;
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUsed;

	public PersistentLogin(PersistentRememberMeToken token) {
		this.series = token.getSeries();
		this.username=token.getUsername();
		this.token = token.getTokenValue();
		this.lastUsed = token.getDate();
	}
}
