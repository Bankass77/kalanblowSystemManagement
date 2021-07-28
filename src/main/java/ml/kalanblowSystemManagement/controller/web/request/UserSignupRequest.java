package ml.kalanblowSystemManagement.controller.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@JsonInclude(content = Include.NON_NULL)
@NoArgsConstructor
public class UserSignupRequest {

	@ApiModelProperty(position = 1)
	private String firstName;

	@ApiModelProperty(position = 2)
	private String lastNme;

	@ApiModelProperty(position = 3)
	private String email;
	
	private String password;

}
