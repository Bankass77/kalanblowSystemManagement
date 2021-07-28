package ml.kalanblowSystemManagement.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class ResponseError {

	private LocalDateTime localDateTime = LocalDateTime.now();
	private String message;
	private String details;
}
